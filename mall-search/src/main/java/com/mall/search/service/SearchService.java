package com.mall.search.service;

import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.utils.JsonUtils;
import com.mall.item.pojo.*;
import com.mall.search.client.BrandClient;
import com.mall.search.client.CategoryClient;
import com.mall.search.client.GoodsClient;
import com.mall.search.client.SpecificationClient;
import com.mall.search.pojo.Goods;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 吧查出来的数据封装到Goods里面
 */
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;

    public Goods buildGoods(Spu spu) {
        Long spuId = spu.getId();
        //查询分类
        List<Category> categoryList =
                categoryClient.getListByIds(
                        Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new MallException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> categoryNameList =
                categoryList.stream().map(Category::getName).collect(Collectors.toList());

        //查询品牌
        Brand brand = brandClient.queryBrandListById(spu.getBrandId());
        if (brand == null) {
            throw new MallException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        List<Sku> skuList = goodsClient.querySkuListBySpuId(spu.getId());
        if (CollectionUtils.isEmpty(skuList)) {
            throw new MallException(ExceptionEnum.SKU_NOT_FOUND);
        }
        List<Map<String, Object>> skus = new ArrayList<>();
        Set<Long> priceList = new HashSet<>();
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("images", StringUtils.split(sku.getImages(), ","));
            skus.add(map);
            priceList.add(sku.getPrice());
        }
        //规格参数
        List<SpecParam> specParams = specificationClient.queryParamListByGid(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(specParams)) {
            throw new MallException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        //查询信息
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);

        if (spuDetail == null) {
            throw new MallException(ExceptionEnum.SPU_DETAIL_NOT_FOUND);
        }
        String genericSpecJson = spuDetail.getGenericSpec();
        String specialSpecJson = spuDetail.getSpecialSpec();
        Map<String, Object> genericMap = JsonUtils.toMap(genericSpecJson);
        Map<String, Object> specialMap = JsonUtils.toMap(specialSpecJson);


        String all = spu.getName() + StringUtils.join(categoryNameList, " ")
                + brand.getName();
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(new Date());
        goods.setId(spu.getId());
        goods.setPrice(priceList);//商品价格的集合
        goods.setSkus(JsonUtils.toString(skus));//商品sku的集合
        goods.setAll("");// TODO
        goods.setSpecs(null);
        goods.setSubTitle(spu.getSubTitle());


        return null;
    }
}
