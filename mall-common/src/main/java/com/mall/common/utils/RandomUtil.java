package com.mall.common.utils;

import java.util.Random;

public class RandomUtil {

    /**
     * 生成len 长度的随机字符串
     *
     * @param len
     * @return
     */
    public static String getRandomString(int len) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int num = random.nextInt(base.length());
            sb.append(base.charAt(num));
        }
        return sb.toString();

    }
}
