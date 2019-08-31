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
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
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
    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
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
        if (count != 1) {
            throw new MallException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
        //保存 spu detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);
      /*  List<Stock> stockList = new ArrayList<>();
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
        count = stockMapper.insertList(stockList);

        if (count != stockList.size()) {
            throw new MallException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
*/
        saveSkuAndStock(spu);
        // 发送消息

    }

    @Transactional
    public void updateGoods(Spu spu) {
        if (spu.getId() == null) {
            throw new MallException(ExceptionEnum.GOODS_ID_CANT_BE_NULL);
        }
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);

        if (!CollectionUtils.isEmpty(skuList)) {
            //  根据实体的属性作为条件 删除sku
            skuMapper.delete(sku);
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            // 删除 stock
            stockMapper.deleteByIdList(ids);
            // 修改 spu
            spu.setValid(null);
            spu.setSaleable(null);
            spu.setLastUpdateTime(new Date());
            spu.setCreateTime(null);
            int count = spuMapper.updateByPrimaryKeySelective(spu);

            if (count != 1) {
                throw new MallException(ExceptionEnum.GOODS_UPDATE_ERROR);
            }
            count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
            if (count != 1) {
                throw new MallException(ExceptionEnum.GOODS_DETAIL_UPDATE_ERROR);
            }
            saveSkuAndStock(spu);
            //发送 mq
            
        }


    }

    private void saveSkuAndStock(Spu spu) {
        int count = 0;
        List<Stock> stockList = new ArrayList<>();
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
        count = stockMapper.insertList(stockList);
        if (count != stockList.size()) {
            throw new MallException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }
    public SpuDetail queryDetailById(Long id) {
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(id);
        SpuDetail res = spuDetailMapper.selectByPrimaryKey(spuDetail);
        if (res == null) {
            throw new MallException(ExceptionEnum.SPU_DETAIL_NOT_FOUND);
        }
        return res;
    }

    public List<Sku> querySkuListBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> list = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(list)) {
            throw new MallException(ExceptionEnum.SKU_NOT_FOUND);
        }
        //查询库存
      /*  for (Sku s : list) {
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if (stock == null) {
                throw new MallException(ExceptionEnum.STOCK_NOT_FOUND);
            }
            s.setStock(stock.getStock());
        }*/

        List<Long> skuIds = list.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = stockMapper.selectByIdList(skuIds);

        if (CollectionUtils.isEmpty(stockList)) {
            throw new MallException(ExceptionEnum.STOCK_NOT_FOUND);
        }

        //skuId与stock的map
        Map<Long, Integer> stockMap =
                stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        list.forEach(i -> i.setStock(stockMap.get(i.getId())));

        return list;
    }

    public Spu querySpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new MallException(ExceptionEnum.SPU__NOT_FOUND);
        }
        //sku list
        List<Sku> skus = querySkuListBySpuId(id);

        //spu detail
        SpuDetail spuDetail = queryDetailById(id);
        spu.setSkus(skus);
        spu.setSpuDetail(spuDetail);
        return spu;
    }
}
