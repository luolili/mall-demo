package com.mall.user.service;

import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.utils.NumberUtils;
import com.mall.user.mapper.UserMapper;
import com.mall.user.pojo.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;
    private static final String KEY_PREFIX = "sms.verify.code";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public Boolean checkData(String data, Integer type) {
        // 判断数据类型
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new MallException(ExceptionEnum.USER_PARAM_ERROR);

        }

        int count = userMapper.selectCount(user);
        return count == 0;
    }

    public void sendCode(String phone) {

        String code = NumberUtils.generateCode(3);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        // 发送验证码
        amqpTemplate.convertAndSend("mall.item.exchange", "user.code", msg);
        // save code into redis
        String key = KEY_PREFIX + phone;
        stringRedisTemplate.opsForValue().set(key, code, 2, TimeUnit.MINUTES);
    }
}
