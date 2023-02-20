package com.sean.community.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    // 随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // md5加密

    public static String md5(String key) {
        return StringUtils.isBlank(key) ? null : DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
