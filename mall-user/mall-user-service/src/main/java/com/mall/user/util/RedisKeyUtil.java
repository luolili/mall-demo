package com.mall.user.util;

public class RedisKeyUtil {

    public static String getLikedKey(String likedUserId, String likedPostId) {
        StringBuilder key = new StringBuilder();
        key.append(likedUserId);
        key.append("::");
        key.append(likedPostId);
        return key.toString();
    }
}
