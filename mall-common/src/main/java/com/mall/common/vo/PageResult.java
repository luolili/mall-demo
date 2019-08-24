package com.mall.common.vo;

import lombok.Data;

import java.util.List;
@Data
public class PageResult<T> {

    private Long total;//总的条数
    private Integer totalPage;//总页数
    private List<T> items;//当前页的数据

    public PageResult() {
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }

    public PageResult(Long total,  List<T> items) {
        this.total = total;
        this.items = items;
    }
}
