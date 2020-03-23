package com.mall.order.dto;

import lombok.Data;

@Data
public class MiaoshaOrderDTO {

    Long userId;
    Long goodsId;
    Integer buyNum;
    String goodsName;

}
