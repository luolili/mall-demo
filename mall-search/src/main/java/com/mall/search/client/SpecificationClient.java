package com.mall.search.client;

import com.mall.item.client.BrandApi;
import com.mall.item.client.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {

}