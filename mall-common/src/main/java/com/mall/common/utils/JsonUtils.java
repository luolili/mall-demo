package com.mall.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static Logger logger= LoggerFactory.getLogger(JsonUtils.class);

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj.getClass() == String.class) {
            return (String)obj;
        }
        try {
            mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.info("json序列化出错"+obj);
        }
        return null;
    }
}
