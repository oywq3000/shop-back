package com.oyproj.modules.mamber.entity.enums;

/**
 * 联合登入枚举
 *
 * @author oywq3000
 * @since 2026-01-24
 */
public enum ConnectEnum {
    /**
     * 联合登录枚举各个类型
     */
    QQ("QQ登录"),
    WEIBO("微博联合登录"),
    //只存放unionid
    WECHAT("微信联合登录"),
    ALIPAY("支付宝登录"),
    APPLE("苹果登录");

    private final String description;
    ConnectEnum(String description) {
        this.description = description;
    }
}
