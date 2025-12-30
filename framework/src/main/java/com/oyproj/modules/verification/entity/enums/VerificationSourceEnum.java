package com.oyproj.modules.verification.entity.enums;

/**
 * 验证码资源枚举
 *
 * @author oywq3000
 * @since 2025-12-30
 */
public enum VerificationSourceEnum {
    /**
     * 滑块
     */
    SLIDER("滑块"),
    /**
     * 验证码源
     */
    RESOURCE("验证码源");

    private final String description;

    VerificationSourceEnum(String des){
        this.description = des;
    }
}
