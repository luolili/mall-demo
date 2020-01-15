package com.mall.user.pojo;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Table(name = "tb_user_like")
@Data
public class UserLike {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long likedUserId;
    private Long likedPostId;
    private Integer status;
    private Date created;
    private Date updated;
}
