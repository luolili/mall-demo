package com.mall.user.util;

public class RedisKeyUtil {

    public static final String MAP_KEY_USER_LIKED = "MAP_KEY_USER_LIKED";
    public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_KEY_USER_LIKED_COUNT";

    public static String getLikedKey(String likedUserId, String likedPostId) {
        StringBuilder key = new StringBuilder();
        key.append(likedUserId);
        key.append("::");
        key.append(likedPostId);
        return key.toString();
    }
}
