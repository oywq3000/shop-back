package com.oyproj.common.security.token.base;

import com.oyproj.common.security.enums.UserEnums;
import com.oyproj.common.security.token.Token;

/**
 * AbstractToken
 * 抽象token，定义生成token类
 *
 * @author oywq3000
 * @since 2026-01-25
 */
public abstract class AbstractTokenGenerate<T> {
    /**
     * 生成token
     */
    public abstract Token createToken(T user,Boolean longTerm);

    /**
     * 刷新token
     */
    public abstract Token refreshToken(String refreshToken);
    /**
     * 默认role
     */
    public UserEnums role = UserEnums.MANAGER;
}
