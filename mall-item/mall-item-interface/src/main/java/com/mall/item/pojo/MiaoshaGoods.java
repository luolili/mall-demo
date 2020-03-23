package com.mall.item.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tb_miaosha_goods")
@Data
public class MiaoshaGoods {
    private Long id;
    private Integer stock;
    private BigDecimal price;
}
