package com.mall.web;

import com.mall.common.utils.Result;
import com.mall.order.sender.CancelOrderSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("order")
public class OrderController {
    @Resource
    private CancelOrderSender cancelOrderSender;

    @GetMapping("cancel")
    public Result cancelOrder() {
        cancelOrderSender.sendMsg(1L, 1000);
        return Result.success(null);
    }
}
