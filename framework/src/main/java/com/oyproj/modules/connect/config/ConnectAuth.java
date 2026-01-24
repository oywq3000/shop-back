package com.oyproj.modules.connect.config;

import com.oyproj.modules.connect.entity.enums.AuthResponseStatus;
import com.oyproj.modules.connect.exception.AuthException;


/**
 * OAuth平台的API地址的统一接口
 *
 * @author oywq3000
 * @since 2026-01-23
 */
public interface ConnectAuth {
    /**
     * 授权的api
     *
     * @return url
     */
    String authorize();
    /**
     * 获取accessToken的api
     *
     * @return url
     */
    String accessToken();

    /**
     * 获取用户信息的api
     *
     * @return url
     */
    String userInfo();

    /**
     * 取消授权的api
     *
     * @return url
     */
    default String revoke() {
        throw new AuthException(AuthResponseStatus.UNSUPPORTED);
    }
    default String getName() {
        if (this instanceof Enum) {
            return String.valueOf(this);
        }
        return this.getClass().getSimpleName();
    }
}
