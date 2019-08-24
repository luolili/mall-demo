package com.mall.search.pojo;

import lombok.Data;

@Data
public class SearchRequest {

    private String key;//关键字查询
    private Integer page;
    private static final int DEFAULT_ROWS = 10;
    private static final int DEFAULT_PAGE = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getPage() {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        return Math.max(DEFAULT_PAGE, page);
    }

    public int getSize() {
        return DEFAULT_ROWS;
    }

}
