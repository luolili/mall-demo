package com.mall.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "mall.order.cancel")
public class CancelOrderReceiver {

    @RabbitHandler
    public void handle(Long orderId) {
        log.info("消息被成功消费");
        log.info("process orderId:[{}]", orderId);
    }
}
