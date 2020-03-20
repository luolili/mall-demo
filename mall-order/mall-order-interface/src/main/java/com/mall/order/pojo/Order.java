package com.mall.order.pojo;

import lombok.Data;

import javax.persistence.Table;

@Table(name = "tb_order")
@Data
public class Order {
    private Long id;
    private String orderNo;
    /**
     * 订单状态
     */
    private Integer status;
}
