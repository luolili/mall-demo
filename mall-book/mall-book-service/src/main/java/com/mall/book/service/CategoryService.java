package com.mall.book.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.book.mapper.CategoryMapper;
import com.mall.book.pojo.Category;
import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {
    @Resource
    CategoryMapper categoryMapper;

    public boolean add(Category category) {

        return categoryMapper.insert(category) == 1;
    }

    public PageResult<Category> query(Integer page, Integer rows, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%");
        }
        List<Category> list = categoryMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            throw new MallException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        PageInfo<Category> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);

    }

    public Category getOne(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    public boolean edit(Category category) {

        return categoryMapper.updateByPrimaryKey(category) == 1;
    }

}
