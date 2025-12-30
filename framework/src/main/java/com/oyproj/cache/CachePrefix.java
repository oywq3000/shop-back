package com.oyproj.cache;

import java.nio.channels.NonReadableChannelException;

/**
 * 缓存前缀
 *
 * @author oywq3000
 * @since 2025-12-30
 */
public enum CachePrefix {
    /**
     * nonce
     */
    NONCE,
    /**
     * 在线人数
     */
    ONLINE_NUM,
    /**
     * 会员分布数据
     */
    MEMBER_DISTRIBUTION,
    /**
     * 在线会员统计
     */
    ONLINE_MEMBER,
    /**
     * token 信息
     */
    ACCESS_TOKEN,
    /**
     * 刷新 token 信息
     */
    REFRESH_TOKEN,
    /**
     * 联合登录响应
     */
    CONNECT_RESULT,
    /**
     * 微信联合登陆 session key
     */
    SESSION_KEY,
    /**
     * 权限
     */
    PERMISSION_LIST,
    /**
     * 部门id列表
     */
    DEPARTMENT_IDS,
    /**
     * 用户错误登录限制
     */
    LOGIN_TIME_LIMIT,
    /**
     * 系统设置
     */
    SETTING,
    /**
     * 验证码滑块源
     */
    VERIFICATION,
    /**
     * 验证码滑块源
     */
    VERIFICATION_IMAGE,
    /**
     * 快递平台
     */
    EXPRESS,
    /**
     * 图片验证码
     */
    CAPTCHA,
    /**
     * 商品
     */
    GOODS,
    /**
     * 商品SKU
     */
    GOODS_SKU,
    /**
     * 运费模板脚本
     */
    SHIP_SCRIPT,
    /**
     * 商品sku
     */
    SKU,
    /**
     * sku库存
     */
    SKU_STOCK,
    /**
     * 促销商品sku库存
     */
    PROMOTION_GOODS_STOCK,
    /**
     * 商品库存
     */
    GOODS_STOCK;

    public static String removePrefix(String str){
        return str.substring(str.lastIndexOf("}_")+2);
    }

    /**
     * 通过获取缓存key值
     *
     * @return 缓存key值
     */
    public String getPrefix(){
        return "{"+this.name()+"}_";
    }

}
