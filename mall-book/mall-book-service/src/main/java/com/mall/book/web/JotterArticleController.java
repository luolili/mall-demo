package com.mall.book.web;

import com.mall.book.pojo.JotterArticle;
import com.mall.book.service.JotterArticleService;
import com.mall.common.utils.ResultVO;
import com.mall.common.vo.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/jotter/article")
public class JotterArticleController {
    @Resource
    JotterArticleService jotterArticleService;

    @GetMapping("/page")
    public ResponseEntity<PageResult<JotterArticle>> getListByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "keywords", required = false) String keywords) {
        return ResponseEntity.ok(jotterArticleService.query(page, rows, keywords));
    }

    @PostMapping("/add")
    public ResultVO add(@RequestBody JotterArticle article) {
        if (jotterArticleService.add(article)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }

    @PostMapping("/get")
    public ResponseEntity getOne(Long articleId) {
        return ResponseEntity.ok(jotterArticleService.getOne(articleId));
    }

    @PutMapping("/edit")
    public ResultVO edit(@RequestBody JotterArticle article) {
        if (jotterArticleService.edit(article)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }

    @DeleteMapping("/del")
    public ResultVO del(@RequestBody JotterArticle article) {
        if (jotterArticleService.del(article)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }
}
