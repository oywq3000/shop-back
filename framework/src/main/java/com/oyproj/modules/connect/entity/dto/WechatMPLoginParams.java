package com.oyproj.modules.connect.entity.dto;


import lombok.Data;

/**
 * 微信登录参数
 */

@Data
public class WechatMPLoginParams {
    /**
     * uuid 用户uuid
     * code 微信返回code 用于与微信交互获取openid等信息
     * encryptedDate 微信返回加密信息
     * iv 微信返回
     * image 微信头像、
     * nickname 微信用户昵称
     */
    private String uuid;
    private String code;
    private String encryptedData;
    private String iv;
    private String image;
    private String nickName;
}
