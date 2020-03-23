package com.mall.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtil {
    /**
     * 反转list列表
     *
     * @param l
     */
    public static void reverse(List<?> l) {
        Collections.reverse(l);
    }

    public static void main(String[] args) {
        List<String> l = new ArrayList<>();
        l.add("one");
        l.add("tt");
        l.add("ss");
        reverse(l);
        l.stream().forEach(System.out::println);
    }
}
