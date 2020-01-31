package com.mall.book.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.book.mapper.BookMapper;
import com.mall.book.pojo.Book;
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
public class BookService {
    @Resource
    BookMapper bookMapper;

    public boolean add(Book book) {

        return bookMapper.insert(book) == 1;
    }

    public PageResult<Book> query(Integer page, Integer rows, Long cid, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Book.class);
        Example.Criteria criteria = example.createCriteria();
        if (cid != null) {
            criteria.andEqualTo("cid", cid);
        }
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
            criteria.orLike("author", "%" + key + "%");
        }
        List<Book> list = bookMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            throw new MallException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        PageInfo<Book> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);

    }

    public Book getOne(Long id) {
        return bookMapper.selectByPrimaryKey(id);
    }

    public boolean edit(Book book) {

        return bookMapper.updateByPrimaryKey(book) == 1;
    }

}
