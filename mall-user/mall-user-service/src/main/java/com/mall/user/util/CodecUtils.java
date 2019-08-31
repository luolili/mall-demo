package com.mall.user.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class CodecUtils {
    public static String md5Hex(String data, String salt) {
        if (StringUtils.isEmpty(salt)) {
            salt = data.hashCode() + "";
        }
        return DigestUtils.md5Hex(salt + DigestUtils.md5Hex(data));
    }
}
