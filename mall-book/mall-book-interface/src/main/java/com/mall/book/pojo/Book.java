package com.mall.book.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_book")
@Data
public class Book {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long cid;
    private String title;
    private String author;
    private String cover;
    private String press;
    private Date created;
    private String abs;

}
