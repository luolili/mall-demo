package com.mall.book.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BookVO {
    private Long id;
    private String cName;
    private String title;
    private String author;
    private String cover;
    private String press;
    private Date created;
    private String abs;
}
