package com.mall.user.service;

public class RedisService {
    /**
     * 点赞状态：1
     *
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void saveLiked2Redis(String likedUserId, String likedPostId) {

    }

    /**
     * 点赞状态：0
     *
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void unlikeFromRedis(String likedUserId, String likedPostId) {

    }

    /**
     * del
     *
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void deleteLikedFromRedis(String likedUserId, String likedPostId) {

    }

    /**
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void incrementLikedCount(String likedUserId, String likedPostId) {

    }

    /**
     * @param likedUserId 被点赞人id
     * @param likedPostId 点赞人id
     */
    void decrementLikedCount(String likedUserId, String likedPostId) {

    }
}
