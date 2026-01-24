package com.oyproj.common.security.enums;

/**
 * 安全相关常量
 *
 * @author oywq3000
 * @since 2026-01-12
 */
public enum SecurityEnum {
    /**
     * 存在与header中的token参数头 名
     */
    HEADER_TOKEN("accessToken"),
    USER_CONTEXT("userContext"),
    JWT_SECRET("secret"),
    UUID("uuid"),
    INVITER("inviter");
    String value;
    SecurityEnum(String value){this.value = value;}
    public String getValue(){return value;}
}
