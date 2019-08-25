package com.mall.item.client;

import com.mall.common.vo.PageResult;
import com.mall.item.pojo.Brand;
import com.mall.item.pojo.Sku;
import com.mall.item.pojo.Spu;
import com.mall.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrandApi {

    @GetMapping("brand/{id}")
    Brand queryBrandListById(@PathVariable("id") Long id);

    @GetMapping("brand/brands")
    List<Brand> queryBrandListByIds(@RequestParam("ids") List<Long> ids);
}
