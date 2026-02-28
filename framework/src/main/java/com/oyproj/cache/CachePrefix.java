package com.oyproj.cache;

import com.oyproj.common.security.enums.UserEnums;

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
     * 刷新token的时间
     */
    REFRESH_TOKEN_TIME,
    /**
     * 邀请人
     */
    INVITER,

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
     * 验证码key
     */
    VERIFICATION_KEY,
    /**
     * 验证码验证结果
     */
    VERIFICATION_RESULT,
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

    GOODS_STOCK,

    /**
     * 短息验证码
     */
    SMS_CODE,

    /**
     * 第三方用户权限
     */
    CONNECT_AUTH;

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

    /**
     * 获取缓存key值+用户端+自定义前缀
     *
     * @param user
     * @param customPrefix
     * @return
     */
    public String getPrefix(UserEnums user,String customPrefix){
        return "{"+this.name()+"_"+user.name()+"}_"+customPrefix+"_";
    }

}
