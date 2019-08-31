package com.mall.user.web;

import com.mall.user.pojo.User;
import com.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //校验 数据
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data") String data,
                                             @PathVariable("type") Integer type) {
        return ResponseEntity.ok(userService.checkData(data, type));

    }

    @PostMapping("code")
    public ResponseEntity<Void> sendCode(String phone) {
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("code")
    public ResponseEntity<Void> register(User user, @RequestParam("code") String code) {
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
