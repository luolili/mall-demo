package com.mall.common.utils;

import com.github.pagehelper.PageInfo;

public class PageUtil {
    public static void copyProperties(PageInfo<?> source, PageInfo<?> des) {
        des.setEndRow(source.getEndRow());
        des.setFirstPage(source.getFirstPage());
        des.setHasNextPage(source.isHasNextPage());
        des.setHasPreviousPage(source.isHasPreviousPage());
        des.setIsFirstPage(source.isIsFirstPage());
        des.setIsLastPage(source.isIsLastPage());
        des.setNavigatepageNums(source.getNavigatepageNums());
        des.setNavigatePages(source.getNavigatePages());
        des.setNextPage(source.getNextPage());
        //des.setOrderBy(source.getOrderBy());
        des.setPageNum(source.getPageNum());
        des.setPages(source.getPages());
        des.setPageSize(source.getPageSize());
        des.setPrePage(source.getPrePage());
        des.setSize(source.getSize());
        des.setStartRow(source.getStartRow());
        des.setTotal(source.getTotal());
    }

}
