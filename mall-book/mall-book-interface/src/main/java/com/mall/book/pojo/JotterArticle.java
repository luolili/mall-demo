package com.mall.book.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_jotter_article")
@Data
public class JotterArticle {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String title;
    private String cover;
    private String contentHtml;
    private String contentMd;
    private Date created;
    // 简介
    private String abs;
    private Integer deleted;

}
