package com.mall.item.client;

import com.mall.common.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface MiaoshaGoodsApi {

    /**
     * 预减库存
     *
     * @param goodsId
     * @param buyNum
     * @return
     */
    @GetMapping("/api/miaosha/goods/decr/stock")
    Result decrStock(@RequestParam("goodsId") Long goodsId, @RequestParam("buyNum") int buyNum);

    @GetMapping("/api/miaosha/goods/decr/stock/mysql")
    Result decrStockByMysql(@RequestParam("goodsId") Long goodsId, @RequestParam("buyNum") int buyNum);
}
