package com.mall.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.vo.PageResult;
import com.mall.item.mapper.*;
import com.mall.item.pojo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;

    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, String saleable, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(key)) {
            criteria.orLike("name", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        example.setOrderByClause("last_update_time desc");
        List<Spu> list = spuMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(list)) {
            throw new MallException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        loadCategoryAndBrandName(list);
        PageInfo<Spu> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);

    }

    private void loadCategoryAndBrandName(List<Spu> list) {

        for (Spu spu : list) {
            //设置分类的名称
            List<Category> categoryList = categoryService.queryCategoryListByIds(
                    Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            List<String> cnameList =
                    categoryList.stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(cnameList, "/"));
            //设置品牌名称
            Long brandId = spu.getBrandId();
            spu.setBname(brandService.queryById(brandId).getName());

        }
    }

    public void saveGoods(Spu spu) {

        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setId(null);
        spu.setSaleable(true);
        spu.setValid(false);
        int count = spuMapper.insert(spu);
        List<Stock> stockList = new ArrayList<>();
        if (count != 1) {
            throw new MallException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
        //保存 spu detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        count = spuDetailMapper.insert(spuDetail);
        //sku
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            //保存sku
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            count = skuMapper.insert(sku);
            if (count != 1) {
                throw new MallException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
            //保存stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            //stockMapper.insert(stock);
            stockList.add(stock);
        }
        //批量新增stock
        stockMapper.insertList(stockList);

    }
}
