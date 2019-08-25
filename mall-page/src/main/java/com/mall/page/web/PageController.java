package com.mall.page.web;

import com.mall.page.service.PageService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("spuId") Long spuId, Model model) {
        //查询商品详情页面需要的数据
        Map<String, Object> attributes = pageService.loadModel(spuId);
        //spring 吧model里面的数据放到thymeleaf里的html页面:context
        model.addAllAttributes(attributes);
        return "item";
    }

}