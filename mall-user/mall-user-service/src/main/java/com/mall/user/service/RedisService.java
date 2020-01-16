package com.mall.user.service;

import com.mall.user.LikedStatusEnum;
import com.mall.user.dto.LikedCountDTO;
import com.mall.user.pojo.UserLike;
import com.mall.user.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RedisService {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 点赞状态：1
     *
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void saveLiked2Redis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtil.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.LIKE.getCode());
    }

    /**
     * 点赞状态：0
     *
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void unlikeFromRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtil.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.UNLIKE.getCode());
    }

    /**
     * del
     *
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void deleteLikedFromRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtil.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.LIKE.getCode());
    }

    /**
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void incrementLikedCount(String likedUserId, String likedPostId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, likedUserId, 1);
    }

    /**
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void decrementLikedCount(String likedUserId, String likedPostId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, likedUserId, -1);
    }

    public List<UserLike> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<UserLike> userLikeList = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split("::");
            String likedUserId = split[0];
            String likedPostId = split[1];

            Integer value = (Integer) entry.getValue();
            UserLike userLike = new UserLike();

            userLike.setLikedUserId(Long.valueOf(likedUserId));
            userLike.setLikedPostId(Long.valueOf(likedPostId));
            userLike.setStatus(value);
            userLikeList.add(userLike);
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_LIKED, key);
        }

        return userLikeList;
    }

    public List<LikedCountDTO> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();

            Integer value = (Integer) entry.getValue();
            LikedCountDTO likedCountDTO = new LikedCountDTO();
            likedCountDTO.setKey(key);
            likedCountDTO.setCount(value);
            list.add(likedCountDTO);
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }
}
