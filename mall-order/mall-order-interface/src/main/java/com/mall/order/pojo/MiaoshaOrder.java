package com.mall.order.pojo;

import lombok.Data;

import javax.persistence.Table;

@Table(name = "tb_miaosha_order")
@Data
public class MiaoshaOrder {
    private Long id;
    private Long goodsId;
    private Long userId;
    private Integer buyNum;
    private String orderNo;
    private String goodsName;
    /**
     * 订单状态
     */
    private Integer status;
}
