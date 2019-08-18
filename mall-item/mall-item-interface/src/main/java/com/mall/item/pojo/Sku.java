package com.mall.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 商品的特殊属性的封装
 */
@Table(name = "tb_sku")
@Data
public class Sku {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String indexes;
    private Boolean enable;
    private Date createTime;
    @JsonIgnore
    private Date lastUpdateTime;

    @Transient
    private Integer stock;//库存

}
