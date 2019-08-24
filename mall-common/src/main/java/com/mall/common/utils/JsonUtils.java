package com.mall.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.info("json序列化出错" + obj);
        }
        return null;
    }

    public static String toString(List<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;

        }
        return JSON.toJSONString(list);
    }

    public static Map<String, Object> toMap(String str) {
        if (StringUtils.isEmpty(str)) {
            return new HashMap<>();
        }

        JSONObject jsonObject = JSONObject.parseObject(str);
        Map<String, Object> map = jsonObject;
        return map;
    }

    public static Map<String, List<Object>> nativeRead(String str) {
        return null;
    }



}
