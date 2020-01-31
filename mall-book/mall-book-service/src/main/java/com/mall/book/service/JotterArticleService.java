package com.mall.book.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.book.mapper.JotterArticleMapper;
import com.mall.book.pojo.JotterArticle;
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
public class JotterArticleService {
    @Resource
    JotterArticleMapper jotterArticleMapper;

    public boolean add(JotterArticle article) {

        return jotterArticleMapper.insert(article) == 1;
    }

    public PageResult<JotterArticle> query(Integer page, Integer rows, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(JotterArticle.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
            criteria.orLike("contentHtml", "%" + key + "%");
            criteria.orLike("contentMd", "%" + key + "%");
        }
        List<JotterArticle> list = jotterArticleMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            throw new MallException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        PageInfo<JotterArticle> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);

    }

    public JotterArticle getOne(Long id) {
        return jotterArticleMapper.selectByPrimaryKey(id);
    }

    public boolean edit(JotterArticle article) {

        return jotterArticleMapper.updateByPrimaryKey(article) == 1;
    }

    public boolean del(JotterArticle article) {
        article.setDeleted(1);
        return jotterArticleMapper.updateByPrimaryKey(article) == 1;
    }
}
