package com.sean.community.community.util;

public interface CommunityConstant {
    /**
     * 激活状态
     * 1 ACTIVATION_SUCCESS 激活成功
     * 2 ACTIVATION_REPREAT 重复激活
     * 3 ACTIVATION_FAILURE 激活失败
     */

    int ACTIVATION_SUCCESS = 0;
    int ACTIVATION_REPREAT = 1;
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认登入凭证超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * "记住我"登入超时时间
     */
    int REMEMBERME_EXPIRED_SECONDS = 3600 * 12 * 100;

}
