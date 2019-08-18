package com.mall.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.vo.PageResult;
import com.mall.item.mapper.BrandMapper;
import com.mall.item.mapper.SpuDetailMapper;
import com.mall.item.mapper.SpuMapper;
import com.mall.item.pojo.Brand;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;


    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortedBy, Boolean desc, String key) {


        PageHelper.startPage(page, rows);
        Example example = new Example(Brand.class);

        if (StringUtils.isNotEmpty(key)) {
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        if (StringUtils.isNotEmpty(sortedBy)) {
            String orderByClause = sortedBy + (desc ? " DESC" : "ASC");
            example.setOrderByClause(orderByClause);
        }

        List<Brand> brands = brandMapper.selectByExample(example);

        PageInfo<Brand> info = new PageInfo<>(brands);
        if (CollectionUtils.isEmpty(brands)) {
            throw new MallException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return new PageResult<Brand>(info.getTotal(), brands);
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if (count != 1) {
            throw new MallException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count != 1) {
                throw new MallException(ExceptionEnum.BRAND_SAVE_ERROR);
            }
        }

    }
}
