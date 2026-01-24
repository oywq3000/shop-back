package com.oyproj.common.security.token;

import lombok.Data;

import java.util.StringTokenizer;

/**
 * Token 实体类
 *
 * @author oywq3000
 * @since 2026-01-23
 */
@Data
public class Token {
    /**
     * 访问token
     */
    private String accessToken;
    /**
     * 刷新token
     */
    private String refreshToken;
}
