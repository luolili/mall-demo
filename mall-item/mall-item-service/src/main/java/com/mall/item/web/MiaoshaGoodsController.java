package com.mall.item.web;

import com.mall.common.utils.Result;
import com.mall.item.service.MiaoshaGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关访问：/api/item是前缀
 */
@RestController
@RequestMapping("/api/miaosha/goods")
public class MiaoshaGoodsController {

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;


    @GetMapping("decr/stock")
    public Result decrStock(@RequestParam("goodsId") Long goodsId, @RequestParam("buyNum") int buyNum) {
        return miaoshaGoodsService.decrStockByRedis(goodsId, buyNum);
    }

    @GetMapping("decr/stock/mysql")
    public Result decrStockByMysql(@RequestParam("goodsId") Long goodsId, @RequestParam("buyNum") int buyNum) {
        return miaoshaGoodsService.decrStockByMysql(goodsId, buyNum);
    }


}