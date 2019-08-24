package com.mall.search.client;

import com.mall.item.client.BrandApi;
import com.mall.item.client.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("item-service")
public interface BrandClient extends BrandApi {

}