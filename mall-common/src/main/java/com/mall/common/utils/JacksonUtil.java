package com.mall.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

public class JacksonUtil {
    public ObjectMapper getObjectMapper() {

        return Holder.mapper;
    }

    private static class Holder {
        private static ObjectMapper mapper = getInstance();

        private static ObjectMapper getInstance() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

            return mapper;
        }
    }
}
