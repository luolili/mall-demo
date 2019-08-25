package com.mall.page.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("spuId") Long spuId, Model model) {


        return "item";
    }
}
