package com.mall.user.pojo;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Table(name = "tb_user")
@Data
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Integer likedNum;
    @NotEmpty
    private String username;
    private String email;
    @JsonIgnore
    private String password;//加密后save
    @Pattern(regexp =
            "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\\\d{8}$"
            , message = "手机号不正确")
    private String phone;
    private String avatar;
    private Date created;
    @JsonIgnore
    private String salt;//密码的盐
    // 1删除0未删除
    private Integer deleted;

}
