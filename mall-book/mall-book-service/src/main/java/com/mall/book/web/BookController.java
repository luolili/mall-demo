package com.mall.book.web;

import com.mall.book.pojo.Book;
import com.mall.book.service.BookService;
import com.mall.common.utils.ResultVO;
import com.mall.common.vo.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Resource
    BookService bookService;


    @GetMapping("/page")
    public ResponseEntity<PageResult<Book>> getListByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "keywords", required = false) String keywords) {
        return ResponseEntity.ok(bookService.query(page, rows, cid, keywords));
    }

    @PostMapping("/add")
    public ResultVO add(@RequestBody Book book) {
        if (bookService.add(book)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }

    @PutMapping("/edit")
    public ResultVO edit(@RequestBody Book book) {
        if (bookService.edit(book)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }
}
