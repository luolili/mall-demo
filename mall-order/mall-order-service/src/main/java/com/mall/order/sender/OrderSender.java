package com.mall.order.sender;

import com.mall.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMsg(Long orderId, final long delayTime) {
        amqpTemplate.convertAndSend("", "", orderId, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(String.valueOf(delayTime));
                return message;
            }
        });
        log.info("send orderId:[{}]", orderId);
    }

    public void sendMiaoshaMsg(MiaoshaMsg msg) {
        log.info("发送秒杀信息");
        amqpTemplate.convertAndSend(RabbitMqConfig.MIAOSHA_QUEUE, msg);
    }
}
