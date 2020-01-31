package com.mall.book.web;

import com.mall.book.pojo.Category;
import com.mall.book.service.CategoryService;
import com.mall.common.utils.ResultVO;
import com.mall.common.vo.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/page")
    public ResponseEntity<PageResult<Category>> getListByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key) {
        return ResponseEntity.ok(categoryService.query(page, rows, key));
    }

    @PostMapping("/add")
    public ResultVO add(@RequestBody Category book) {
        if (categoryService.add(book)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }

    @PutMapping("/edit")
    public ResultVO edit(@RequestBody Category book) {
        if (categoryService.edit(book)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }
}
