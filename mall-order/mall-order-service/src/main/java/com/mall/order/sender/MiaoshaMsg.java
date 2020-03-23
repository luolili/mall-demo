package com.mall.order.sender;

import lombok.Data;

@Data
public class MiaoshaMsg {

    private Long userId;
    private Long goodsId;
    private Integer buyNum;
}
