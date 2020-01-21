package com.mall.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.utils.NumberUtils;
import com.mall.common.vo.PageResult;
import com.mall.user.event.UserRegisterEvent;
import com.mall.user.mapper.UserMapper;
import com.mall.user.pojo.User;
import com.mall.user.util.CodecUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;
    private static final String KEY_PREFIX = "sms.verify.code";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public User getUserByUsernameAndPassword(String username, String password) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            criteria.andEqualTo("username", username);
            criteria.andEqualTo("password", password);
        }
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users.get(0);
    }

    public PageResult<User> query(Integer page, Integer rows, Boolean saleable, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        List<User> list = userMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(list)) {
            throw new MallException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        PageInfo<User> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);

    }

    public User getOne(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public void updateLikedCount(User user) {

        userMapper.updateByPrimaryKey(user);
    }
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

    public void register(User user, String code) {
        // 验证码验证
        String aCode = stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());

        if (!StringUtils.equals(aCode, code)) {
            throw new MallException(ExceptionEnum.USER_PARAM_ERROR);
        }
        // 对密码 加密
        DigestUtils.md5Hex(user.getPassword());
        String salt = "1;";
        String md5Password = CodecUtils.md5Hex(user.getPassword(), salt);

        user.setPassword(md5Password);
        user.setSalt(salt);
        user.setCreated(new Date());
        userMapper.insert(user);

    }

    public User queryByUsernameAndPassword(String username, String password) {
        User user = new User();
        user.setUsername(username);
        //user.setPassword(password);
        User one = userMapper.selectOne(user);
        if (one == null) {
            throw new MallException(ExceptionEnum.USER_INVALID);
        }

        if (!StringUtils.equals(one.getPassword(), CodecUtils.md5Hex(password, user.getSalt()))) {
            throw new MallException(ExceptionEnum.USER_INVALID);
        }
        return one;
    }

    public void register() {

        applicationContext.publishEvent(new UserRegisterEvent(this, "1"));
    }
}
