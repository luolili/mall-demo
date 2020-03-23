package com.mall.receiver;

import com.mall.common.utils.Result;
import com.mall.order.client.MiaoshaGoodsClient;
import com.mall.order.sender.MiaoshaMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j

public class OrderReceiver {
    private final String KEY_PREFIX = "goodsId_";
    @Resource
    private MiaoshaGoodsClient miaoshaGoodsClient;
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = "mall.order.cancel")
    //@RabbitHandler
    public void handle(Long orderId) {
        log.info("消息被成功消费");
        log.info("process orderId:[{}]", orderId);
    }

    public void receiveMiaoshaMsg(MiaoshaMsg msg) {
        log.info("接受到秒杀信息");
        Long goodsId = msg.getGoodsId();
        Integer buyNum = msg.getBuyNum();
        // 数据库减库存
        Result res = miaoshaGoodsClient.decrStockByMysql(goodsId, buyNum);
        if (!"success".equals(res.getMsg())) {
            redisTemplate.opsForValue().increment(KEY_PREFIX + goodsId, buyNum);
        }


    }
}
