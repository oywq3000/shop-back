package com.oyproj.cache.limit.enums;

/**
 * @author oywq3000
 * @since 2025-12-30
 */
public enum LimitTypeEnums {
    /**
     * 自定义key(即全局限流)
     */
    CUSTOMER,
    /**
     * 根据请求者的IP(IP限流)
     */
    IP
}
