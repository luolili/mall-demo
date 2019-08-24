package com.mall.repo;

import com.mall.common.vo.PageResult;
import com.mall.item.pojo.Spu;
import com.mall.search.client.GoodsClient;
import com.mall.search.pojo.Goods;
import com.mall.search.service.SearchService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void testAddData() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
        //查出所有spu
        int size = 0;
        int page = 1;
        int rows = 2;
        do {
            PageResult<Spu> result =
                    goodsClient.getListByPage(page, rows, true, null);
            List<Spu> items = result.getItems();
            if (CollectionUtils.isEmpty(items)) {
                break;
            }
            List<Goods> goodsList =
                    items.stream()
                            .map(searchService::buildGoods).collect(Collectors.toList());
            //放入es
            goodsRepo.saveAll(goodsList);
            page++;
            size = items.size();
        } while (size == 100);

    }

}