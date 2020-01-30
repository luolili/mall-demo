package com.mall.common.utils;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

/**
 * 解决redis缓存穿透
 */
public class BloomFilterUtil {

    /**
     * @param str  要搜索的字符串
     * @param num  数据量
     * @param rate 容错率
     * @return
     */
    public static boolean filter(String str, int num, double rate) {
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), num, rate);

        bloomFilter.put("sr");
        return bloomFilter.mightContain("sr");
    }
}
