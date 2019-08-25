package com.mall.page.service;

import com.mall.item.pojo.*;
import com.mall.page.client.BrandClient;
import com.mall.page.client.CategoryClient;
import com.mall.page.client.GoodsClient;
import com.mall.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PageService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;

    public Map<String, Object> loadModel(Long spuId) {
        Map<String, Object> map = new HashMap<>();
        Spu spu = goodsClient.querySpuBySpuId(spuId);
        List<Sku> skus = spu.getSkus();
        SpuDetail detail = spu.getSpuDetail();
        Brand brand = brandClient.queryBrandListById(spu.getBrandId());
        List<Category> categoryList = categoryClient.getListByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        List<SpecGroup> specGroups = specificationClient.queryGroupListByGid(spu.getCid3());
        map.put("spu", spu);
        map.put("skus", skus);
        map.put("detail", detail);
        map.put("brand", brand);
        map.put("catagoryList", categoryList);
        map.put("specGroups", specGroups);
        return map;
    }

    @Autowired
    private TemplateEngine templateEngine;

    public void createHtml(Long spuId) {
        Context context = new Context();

        context.setVariables(loadModel(spuId));
        File file = new File("F:\\githubpro\\mall-demo\\upload", spuId + ".html");

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            templateEngine.process("item", context, writer);
        } catch (FileNotFoundException e) {
            log.info("--文件未找到");
        } catch (UnsupportedEncodingException e) {
            log.info("--页面静态化异常");
        }

    }
}
