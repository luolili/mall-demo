package com.mall.page;

import com.mall.page.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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


        model.addAttribute("user", user);
        return "halo";//view
    }
}
