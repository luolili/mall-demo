package com.mall;

import com.mall.pojo.Item;
import com.mall.repo.ItemRepo;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
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
        items.add(new Item(5L, "apple", "mee3", "t5t3", 1.01, "http:wee3"));
        repository.saveAll(items);
    }

    @Test
    public void findData() {
        Iterable<Item> items = repository.findAll();
        items.forEach(System.out::println);
    }

    /**
     * 复杂查询，需要写es查询语句
     * Item(id=1, title=apple, category=df, brand=tt, price=33.22, images=http:de)
     * Item(id=2, title=fv1, category=mee, brand=t5t, price=11.21, images=http:wee)
     */
    @Test
    public void findDataByPrice() {
        List<Item> items = repository.findByPriceBetween(2.01, 33.66);
        items.forEach(System.out::println);
    }

    /**
     * QueryBuilder 来自org.es
     */
    @Test
    public void nativeSearch() {
        //from spring
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // MatchQueryBuilder只能条件查询
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("title", "apple");
        nativeSearchQueryBuilder.withQuery(matchQuery);//加条件的分页
        //对结果进行filter
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "title", "price"}, null));
        //排序
        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        //分页,页数从0开始
        nativeSearchQueryBuilder.withPageable(PageRequest.of(0, 2));

        SearchQuery query = nativeSearchQueryBuilder.build();
        // Iterable<Item> items = repository.search(matchQuery);
        Page<Item> items = repository.search(query);
        long totalElements = items.getTotalElements();
        int totalPages = items.getTotalPages();
        int size = items.getSize();

        items.forEach(System.out::println);
        List<Item> content = items.getContent();
        content.forEach(System.out::println);
        System.out.println(totalElements + "--" + totalPages);
    }

    /**
     *
     */
    @Test
    public void testAggregation() {
        //from spring
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //聚合的名字
        TermsAggregationBuilder popularBrand = AggregationBuilders.terms("popularBrand");
        nativeSearchQueryBuilder.addAggregation(popularBrand.field("brand"));
        AggregatedPage<Item> result = template.queryForPage(nativeSearchQueryBuilder.build(), Item.class);
        // 获得agg集合
        Aggregations aggregations = result.getAggregations();
        // 根据agg name 获取agg
        StringTerms terms = aggregations.get("popularBrand");
        // 获得桶
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            System.out.println("bucket key: " + bucket.getKeyAsString());
            System.out.println("bucket doc count: " + bucket.getDocCount());

        }


    }
}
