package com.mall.search.service;

import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.utils.JsonUtils;
import com.mall.common.vo.PageResult;
import com.mall.item.pojo.*;
import com.mall.repo.GoodsRepo;
import com.mall.search.client.BrandClient;
import com.mall.search.client.CategoryClient;
import com.mall.search.client.GoodsClient;
import com.mall.search.client.SpecificationClient;
import com.mall.search.pojo.Goods;
import com.mall.search.pojo.SearchRequest;
import com.mall.search.pojo.SearchResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 吧查出来的数据封装到Goods里面
 */
@Service
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepo goodsRepo;

    @Autowired
    private ElasticsearchTemplate template;

    public Goods buildGoods(Spu spu) {
        Long spuId = spu.getId();
        //查询分类
        List<Category> categoryList =
                categoryClient.getListByIds(
                        Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new MallException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> categoryNameList =
                categoryList.stream().map(Category::getName).collect(Collectors.toList());

        //查询品牌
        Brand brand = brandClient.queryBrandListById(spu.getBrandId());
        if (brand == null) {
            throw new MallException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        List<Sku> skuList = goodsClient.querySkuListBySpuId(spu.getId());
        if (CollectionUtils.isEmpty(skuList)) {
            throw new MallException(ExceptionEnum.SKU_NOT_FOUND);
        }
        List<Map<String, Object>> skus = new ArrayList<>();
        Set<Long> priceList = new HashSet<>();
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("images", StringUtils.split(sku.getImages(), ","));
            skus.add(map);
            priceList.add(sku.getPrice());
        }
        //规格参数
        List<SpecParam> specParams = specificationClient.queryParamListByGid(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(specParams)) {
            throw new MallException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        //查询信息
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);

        if (spuDetail == null) {
            throw new MallException(ExceptionEnum.SPU_DETAIL_NOT_FOUND);
        }
        String genericSpecJson = spuDetail.getGenericSpec();
        String specialSpecJson = spuDetail.getSpecialSpec();
        Map<String, Object> genericMap = JsonUtils.toMap(genericSpecJson);
        Map<String, List<Object>> specialMap = JsonUtils.nativeRead(specialSpecJson);

        Map<String, Object> specs = new HashMap<>();
        specs = change(specParams, genericMap, specialMap);

        String all = spu.getName() + StringUtils.join(categoryNameList, " ")
                + brand.getName();
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(new Date());
        goods.setId(spu.getId());
        goods.setPrice(priceList);//商品价格的集合
        goods.setSkus(JsonUtils.toString(skus));//商品sku的集合
        goods.setAll(all);
        goods.setSpecs(specs);
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }

    private Map<String, Object> change(List<SpecParam> params, Map<String, Object> genericMap,
                                       Map<String, List<Object>> specialMap) {
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            String name = param.getName();//key
            Object value = "";
            //判断是否是generic
            if (param.getGeneric()) {
                value = genericMap.get(String.valueOf(param.getId()));
                if (param.getNumeric()) {
                    value = chooseSegment(value.toString(), param);
                }
            } else {
                value = specialMap.get(String.valueOf(param.getId()));
            }
            specs.put(name, value);
        }
        return specs;
    }

    private String chooseSegment(String value, SpecParam param) {
        double val = NumberUtils.toDouble(value);

        String result = "other";
        for (String segment : param.getSegments().split(",")) {
            String[] args = segment.split("-");
            double begin = NumberUtils.toDouble(args[0]);

            double end = Double.MAX_VALUE;
            if (args.length == 2) {
                end = NumberUtils.toDouble(args[1]);
            }
            if (val >= begin && val < end) {
                if (args.length == 1) {
                    result = args[0] + param.getUnit() + "以上";
                } else if (begin == 0) {
                    result = args[1] + param.getUnit() + "以下";
                } else {
                    result = segment + param.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest request) {
        int page = request.getPage() - 1;// 页数从0开始
        int size = request.getSize();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder
                .withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        // 2 分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(page, size));

        // 3 filter
        QueryBuilder matchQuery = buildBasicQuery(request);

        nativeSearchQueryBuilder
                .withQuery(matchQuery);

        // 4 agg
        String categoryAggName = "category_agg";
        String brandAggName = "brand_agg";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName)
                .field("cid3"));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(brandAggName)
                .field("brandId"));

        AggregatedPage<Goods> goodsPage =
                template.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);

        // 5 agg 解析
        Aggregations aggregations = goodsPage.getAggregations();
        Aggregation categoryAgg = aggregations.get(categoryAggName);
        Aggregation brandAgg = aggregations.get(brandAggName);
        List<Category> categoryList = parseCategoryAgg((LongTerms) categoryAgg);
        List<Brand> brandList = parseBrandAgg((LongTerms) brandAgg);
        // 6 specs
        List<Map<String, Object>> specs = null;

        if (categoryList != null && categoryList.size() == 1) {
            //开始聚合
            specs = buildSpecificationAgg(categoryList.get(0).getId(), matchQuery);
        }
        // 7
        long total = goodsPage.getTotalElements();
        int totalPages = goodsPage.getTotalPages();
        List<Goods> content = goodsPage.getContent();
        return new SearchResult(total, totalPages, content, categoryList, brandList, specs);

    }

    private QueryBuilder buildBasicQuery(SearchRequest request) {
        //准备 boolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()));
        //过滤条件
        Map<String, String> filter = request.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            if (!"cid3".equals(key) && "brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }
            String value = entry.getValue();
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, value));

        }
        return boolQueryBuilder;
    }

    private List<Map<String, Object>> buildSpecificationAgg(Long cid, QueryBuilder matchQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        // 1 查询需要聚合的规格参数
        List<SpecParam> specParams = specificationClient.queryParamListByGid(null, cid, true);

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(matchQuery);

        // 2 聚合
        for (SpecParam param : specParams) {
            String name = param.getName();
            nativeSearchQueryBuilder.
                    addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));

        }
        //3 获得结果
        AggregatedPage<Goods> result =
                template.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
        // 4 解析结果
        Aggregations aggregations = result.getAggregations();
        for (SpecParam param : specParams) {
            String name = param.getName();
            StringTerms terms = aggregations.get(name);
            List<String> options =
                    terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList());
            Map<String, Object> map = new HashMap<>();
            map.put("k", name);
            map.put("options", options);

            specs.add(map);

        }


        return specs;

    }

    private List<Category> parseCategoryAgg(LongTerms aggregation) {
        try {
            List<Long> ids = aggregation.getBuckets()
                    .stream()
                    .map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            return categoryClient.getListByIds(ids);
        } catch (Exception e) {
            return null;
        }
    }

    private List<Brand> parseBrandAgg(LongTerms aggregation) {
        try {
            List<Long> ids = aggregation.getBuckets()
                    .stream()
                    .map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            return brandClient.queryBrandListByIds(ids);
        } catch (Exception e) {
            return null;
        }
    }


    public void createOrUpdateIndex(Long spuId) {

        Spu spu = goodsClient.querySpuBySpuId(spuId);
        Goods goods = buildGoods(spu);
        goodsRepo.save(goods);

    }

    public void deleteIndex(Long spuId) {
        goodsRepo.deleteById(spuId);
    }
}
