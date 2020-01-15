package com.mall.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.utils.NumberUtils;
import com.mall.common.vo.PageResult;
import com.mall.user.mapper.UserLikeMapper;
import com.mall.user.mapper.UserMapper;
import com.mall.user.pojo.User;
import com.mall.user.pojo.UserLike;
import com.mall.user.util.CodecUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserLikeService {
    @Autowired
    private UserLikeMapper userLikeMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<UserLike> query(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(UserLike.class);
        List<UserLike> list = userLikeMapper.selectByExample(example);

        PageInfo<UserLike> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);

    }

    public PageResult<UserLike> queryByLikedUserId(Integer page, Integer rows, Long likedUserId) {
        PageHelper.startPage(page, rows);
        Example example = new Example(UserLike.class);
        Example.Criteria criteria = example.createCriteria();
        if (likedUserId != null) {
            criteria.andEqualTo("likedUserId", likedUserId);
        }
        List<UserLike> list = userLikeMapper.selectByExample(example);
        PageInfo<UserLike> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);

    }

    public PageResult<UserLike> queryByLikedPostId(Integer page, Integer rows, Long likedPostId) {
        PageHelper.startPage(page, rows);
        Example example = new Example(UserLike.class);
        Example.Criteria criteria = example.createCriteria();
        if (likedPostId != null) {
            criteria.andEqualTo("likedPostId", likedPostId);
        }
        List<UserLike> list = userLikeMapper.selectByExample(example);
        PageInfo<UserLike> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);

    }

}