package com.mall.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.utils.NumberUtils;
import com.mall.common.vo.PageResult;
import com.mall.user.LikedStatusEnum;
import com.mall.user.dto.LikedCountDTO;
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
import org.springframework.transaction.annotation.Transactional;
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
    private RedisService redisService;
    @Autowired
    private UserService userService;
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
        criteria.andEqualTo("status", LikedStatusEnum.LIKE.getCode());
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
        criteria.andEqualTo("status", LikedStatusEnum.LIKE.getCode());
        if (likedPostId != null) {
            criteria.andEqualTo("likedPostId", likedPostId);
        }
        List<UserLike> list = userLikeMapper.selectByExample(example);
        PageInfo<UserLike> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);

    }

    public UserLike queryByLikedUserIdAndLikedPostId(Long likedUserId, Long likedPostId) {
        Example example = new Example(UserLike.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", LikedStatusEnum.LIKE.getCode());
        if (likedUserId != null) {
            criteria.andEqualTo("likedUserId", likedUserId);
        }
        if (likedPostId != null) {
            criteria.andEqualTo("likedPostId", likedPostId);
        }
        return userLikeMapper.selectByExample(example).get(0);

    }

    public void transLikedFromRedis2DB() {
        List<UserLike> data = redisService.getLikedDataFromRedis();
        for (UserLike userLike : data) {
            UserLike ul = queryByLikedUserIdAndLikedPostId(userLike.getLikedUserId(), userLike.getLikedPostId());
            if (ul == null) {
                save(ul);
            } else {
                ul.setStatus(userLike.getStatus());
                save(ul);
            }
        }

    }

    public void transLikedCountFromRedis2DB() {
        List<LikedCountDTO> dtos = redisService.getLikedCountFromRedis();

        for (LikedCountDTO dto : dtos) {
            User u = userService.getOne(dto.getId());
            if (u != null) {
                Integer likedNum = u.getLikedNum() + dto.getCount();
                u.setLikedNum(likedNum);
                userService.updateLikedCount(u);
            }


        }
    }

    @Transactional
    public UserLike save(UserLike userLike) {

        int count = userLikeMapper.insert(userLike);

        return userLike;
    }

    public List<UserLike> saveAll(List<UserLike> userLikeList) {
        return null;
    }

}