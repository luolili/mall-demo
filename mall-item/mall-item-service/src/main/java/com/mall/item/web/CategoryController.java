package com.mall.item.web;

import com.mall.item.pojo.Category;
import com.mall.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 网关访问：/api/item是前缀
 *
 * ajax请求不允许跨域，出于安全考虑：cookie登陆信息
 * 解决方法：
 *1. jsonp: 需要服务器的支持，只支持get请求
 * 2. cors: 支持各种请求；在服务端控制
 * 浏览器把ajax分为：简单请求；特殊请求
 * 简单请求：请求头携带origin：当前网站域名--后台服务器：决定是否跨域--
 * 允许：响应头：包含当前网站域名+access control allow credential
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> getListByPid(@RequestParam("pid") Long pid) {
        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }

    //根据多个分类id查询分类
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> getListByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.queryCategoryListByIds(ids));
    }

}
