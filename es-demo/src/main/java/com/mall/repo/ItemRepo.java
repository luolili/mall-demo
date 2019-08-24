package com.mall.repo;

import com.mall.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepo extends ElasticsearchRepository<Item, Long> {
    //带命名规则的查询
    List<Item> findByPriceBetween(Double begin, Double end);

}
