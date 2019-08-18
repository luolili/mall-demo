package com.mall.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 商品的通用属性的封装
 */
@Table(name = "tb_spu")
@Data
public class Spu {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long brandId;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private String name;
    private String subTitle;
    private Boolean saleable;
    private Boolean valid;
    private Date createTime;
    @JsonIgnore
    private Date lastUpdateTime;

    @Transient
    private String cname;
    @Transient
    private String bname;
    //前台json数据对象最外层spu:spu包含多个sku, 一个spu detail
    @Transient
    private List<Sku> skus;
    @Transient
    private SpuDetail spuDetail;


}
