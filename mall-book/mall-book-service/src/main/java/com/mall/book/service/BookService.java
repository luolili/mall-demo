package com.mall.book.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.book.mapper.BookMapper;
import com.mall.book.mapper.CategoryMapper;
import com.mall.book.pojo.Book;
import com.mall.book.pojo.Category;
import com.mall.book.vo.BookVO;
import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@Service
public class BookService {
    @Resource
    BookMapper bookMapper;
    @Resource
    CategoryMapper categoryMapper;

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

    /**
     * spring 遇到 @Transactional 会先获取一个数据库连接
     *
     * @param id
     * @return
     */
    //@Transactional 用编程是事务，缩小事务的范围
    public Book getOne(Long id) {
        return bookMapper.selectByPrimaryKey(id);
    }

    public BookVO getOneWithCategory(Long id) {
        BookVO bookVO = new BookVO();
        //查询书:查询书和分类可能需要从其他系统调用，普通写法是串行执行
        Book book = bookMapper.selectByPrimaryKey(id);
        Long cid = book.getCid();
        //查询分类
        Category category = categoryMapper.selectByPrimaryKey(cid);
        //多线程执行
        Callable<Book> bookCallable = new Callable<Book>() {
            @Override
            public Book call() throws Exception {
                return bookMapper.selectByPrimaryKey(id);
            }
        };
        FutureTask<Book> bookTask = new FutureTask<>(bookCallable);
        Callable<Category> categoryCallable = new Callable<Category>() {
            @Override
            public Category call() throws Exception {
                return categoryMapper.selectByPrimaryKey(cid);
            }
        };
        FutureTask<Category> categoryTask = new FutureTask<>(categoryCallable);


        new Thread(bookTask).start();
        new Thread(categoryTask).start();

        try {
            Book bookFromTask = bookTask.get();
            Category cateFromTask = categoryTask.get();
            BeanUtils.copyProperties(bookFromTask, bookVO);
            bookVO.setCName(cateFromTask.getName());
        } catch (Exception e) {

        }

        return bookVO;
    }

    public boolean edit(Book book) {

        return bookMapper.updateByPrimaryKey(book) == 1;
    }

}
