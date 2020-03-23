package com.mall.order.client;

import com.mall.item.client.MiaoshaGoodsApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("item-service")
public interface MiaoshaGoodsClient extends MiaoshaGoodsApi {

}