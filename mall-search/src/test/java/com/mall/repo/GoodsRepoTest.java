package com.mall.repo;

import com.mall.search.pojo.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepoTest {

    @Autowired
    private GoodsRepo goodsRepo;

    @Autowired
    private ElasticsearchTemplate template;

    /**
     * GET /goods
     */
    @Test
    public void testAddIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }
}