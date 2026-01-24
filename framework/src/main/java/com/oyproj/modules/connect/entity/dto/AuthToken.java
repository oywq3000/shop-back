package com.oyproj.modules.connect.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author oywq3000
 * @since 2026-01-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken implements Serializable {
    private static final long serialVersionUID = -2701476618576443366L;
    /**
     * 第三方token
     */
    private String accessToken;
    /**
     * 第三方刷新token
     */
    private String refreshToken;
    /**
     * 有效时间
     */
    private int expireIn;
    /**
     * 会员id
     */
    private String uid;
    /**
     * 联合登录id
     */
    private String unionId;
    /**
     * 联合登录openid
     */
    private String openId;
    /**
     * 请求码
     */
    private String accessCode;
    /**
     * 范围
     */
    private String scope;
}
