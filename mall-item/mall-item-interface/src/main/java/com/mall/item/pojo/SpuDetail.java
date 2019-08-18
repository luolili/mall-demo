package com.mall.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_spu_detail")
@Data
public class SpuDetail {

    @Id
    private Long spuId;
    private String description;
    private String genericSpec;
    private String specialSpec;
    private String packingList;
    private String afterService;


}
