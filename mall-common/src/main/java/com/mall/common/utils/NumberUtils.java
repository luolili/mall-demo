package com.mall.common.utils;

public class NumberUtils {

    //i 表示要生成的位数+1
    public static String generateCode(int i) {
        int re = (int) ((Math.random() * 9 + 1) * Math.pow(10, i - 1));
        return String.valueOf(re);
    }

    public static void main(String[] args) {
        String s = generateCode(6);

    }
}
