package com.mall.user.service;

import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.user.mapper.UserMapper;
import com.mall.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

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
}
