package com.oyproj.modules.connect.entity.enums;

import com.oyproj.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 用户性别
 *
 * @author oywq3000
 * @since 2026-01-23
 */
@Getter
@AllArgsConstructor
public enum AuthUserGender {
    MALE("1","男"),
    FEMALE("0","女"),
    UNKNOWN("-1", "未知");

    private final String code;
    private final String desc;

    /**
     * 获取用户的实际性别，常规网站
     */
    public static AuthUserGender getRealGender(String originalGender){
        if(originalGender==null||UNKNOWN.getCode().equals(originalGender)){
            return UNKNOWN;
        }
        String[] males = {"m","男","1","male"};
        if(Arrays.asList(males).contains(originalGender.toLowerCase())){
            return MALE;
        }
        return FEMALE;
    }

    /**
     * 获取微信平台用户的实际性别，0表示未定义，1表示男性，2表示女性
     */
    public static AuthUserGender getWechatRealGender(String originalGender){
        String noGender = "0";
        if(StringUtils.isEmpty(originalGender)||noGender.equals(originalGender)){
            return AuthUserGender.UNKNOWN;
        }
        return getRealGender(originalGender);
    }

}
