package com.mall.repo;

import com.mall.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepo extends ElasticsearchRepository<Goods, Long> {
}
