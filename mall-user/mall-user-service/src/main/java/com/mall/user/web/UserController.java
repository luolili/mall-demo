package com.mall.user.web;

import com.mall.common.constants.CommonConst;
import com.mall.common.utils.ResultVO;
import com.mall.common.vo.PageResult;
import com.mall.user.pojo.User;
import com.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * 每个service接口  是无状态的
 * 登陆：
 * json web token : jwt 授权中心， rsa加密
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("user/login")
    public ResultVO login(@RequestBody User user, HttpSession session) {
        String username = user.getUsername();
        String pwd = user.getPassword();
        User u = userService.getUserByUsernameAndPassword(username, pwd);
        if (u == null) {
            return new ResultVO(400);
        } else {
            session.setAttribute(CommonConst.USER_SESSION, u);
            return new ResultVO(200);
        }
    }

    @PostMapping("user/add")
    public ResultVO add(@RequestBody User user) {
        if (userService.add(user)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }

    @PutMapping("user/edit")
    public ResultVO edit(@RequestBody User user) {
        if (userService.edit(user)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }

    @PutMapping("user/reset")
    public ResultVO resetPassword(String email, String newPassword) {
        if (userService.resetPassword(email, newPassword)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }


    @DeleteMapping("user/del")
    public ResultVO del(User user) {
        if (userService.delete(user)) {
            return new ResultVO(200);
        }
        return new ResultVO(400);
    }

    @GetMapping("user/page")
    public ResponseEntity<PageResult<User>> getListByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key) {
        return ResponseEntity.ok(userService.query(page, rows, key));
    }


    //校验 数据
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data") String data,
                                             @PathVariable("type") Integer type) {
        return ResponseEntity.ok(userService.checkData(data, type));

    }

    /* @PostMapping("code")
     public ResponseEntity<Void> sendCode(String phone) {
         userService.sendCode(phone);
         return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

 */
    @PostMapping("code")
    public ResponseEntity<Void> register(@Valid User user,
                                         BindingResult result,
                                         @RequestParam("code") String code) {
        //自己处理 field errs
        if (result.hasErrors()) {
            throw new RuntimeException(result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("|")));
        }
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
