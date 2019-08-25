package com.mall.page.client;

import com.mall.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface CategoryClient {

    @GetMapping("category/list/ids")
    List<Category> getListByIds(@RequestParam("ids") List<Long> ids);
}
