package com.mall.mq;

import com.mall.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListener {

    @Autowired
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.insert.queue", durable = "true"),
            exchange = @Exchange(value = "mall.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))
    public void listenInsertOrUpdate(Long spuId) {
        if (spuId == null) {
            return;
        }
        //存入索引库
        searchService.createOrUpdateIndex(spuId);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.delete.queue", durable = "true"),
            exchange = @Exchange(value = "mall.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void listenDelete(Long spuId) {
        if (spuId == null) {
            return;
        }
        //delete索引库
        searchService.deleteIndex(spuId);

    }
}
