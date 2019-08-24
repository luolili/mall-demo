package com.mall.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {

    @Id
    Long id;//spuId
    @Field(type = FieldType.Text, analyzer = "id_max_word")
    String all;//所有的搜索信息：标题，品牌，分类
    @Field(type = FieldType.Keyword, index = false)
    String subTitle;//卖点

    private Long brandId;
    private Long cid1;
    private Long cid2;
    private Long cid3;

    private Date createTime;//spu创建时间
    private Set<Long> price;
    @Field(type = FieldType.Keyword, index = false)
    private String skus;//json

    private Map<String, Object> specs;//key:参数名，value：参数值

}
