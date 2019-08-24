package com.mall;

import com.mall.pojo.Item;
import com.mall.repo.ItemRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ESTest {

    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public void testCreateIndex() {
        //创建index
        template.createIndex(Item.class);
        template.putMapping(Item.class);
    }

    @Autowired
    ItemRepo repository;//用于crud,paging
    //ElasticsearchRepository repository;//用于crud,paging

    /**
     * 批量add
     * GET /hu/_search
     * {
     * "query": {"match_all": {}}
     * }
     */
    @Test
    public void addData() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "apple", "df", "tt", 33.22, "http:de"));
        items.add(new Item(2L, "fv1", "mee", "t5t", 11.21, "http:wee"));
        items.add(new Item(3L, "fv2", "mee2", "t5t2", 1.21, "http:wee2"));
        items.add(new Item(4L, "fv3", "mee3", "t5t3", 1.1, "http:wee3"));
        repository.saveAll(items);
    }

    @Test
    public void findData() {
        Iterable<Item> items = repository.findAll();
        items.forEach(System.out::println);
    }

    @Test
    public void findDataByPrice() {
        List<Item> items = repository.findByPriceBetween(2.01, 33.66);
        items.forEach(System.out::println);
    }

}
