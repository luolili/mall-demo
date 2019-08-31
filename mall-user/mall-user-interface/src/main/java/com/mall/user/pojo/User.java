package com.mall.user.pojo;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table
@Data
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;//加密后save
    private String phone;
    private Date created;
    @JsonIgnore
    private String salt;//密码的盐
}
