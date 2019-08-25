package com.mall.item.client;

import com.mall.common.vo.PageResult;
import com.mall.item.pojo.Sku;
import com.mall.item.pojo.Spu;
import com.mall.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {

    //根据Id查询商品spu
    @GetMapping("/spu/{id}")
    Spu querySpuBySpuId(@PathVariable("id") Long id);

    @GetMapping("spu/page")
    PageResult<Spu> getListByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    @GetMapping("/spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable("id") Long id);

    @GetMapping("/sku/{spuId}")
    List<Sku> querySkuListBySpuId(@PathVariable("spuId") Long spuId);
}
