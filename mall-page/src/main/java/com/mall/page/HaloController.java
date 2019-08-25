package com.mall.page;

import com.mall.page.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class HaloController {

    @GetMapping("halo")
    public String halo(Model model) {
        model.addAttribute("msg", "halo ,");
        return "halo";//view
    }

    @GetMapping("test01")
    public String test01(Model model) {
        User user = new User();
        user.setName("xi");
        user.setAge(12);
        user.setFriend(new User("li", 11, null));
        User user1 = new User("tg", 33, null);

        model.addAttribute("user", user);
        model.addAttribute("users", Arrays.asList(user, user1));
        return "halo";//view
    }

}
