package com.oyproj.cache.limit.annotation;


import com.oyproj.cache.limit.enums.LimitTypeEnums;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.*;

/**
 * limit the user point count
 *
 * @author oywq3000
 * @since 2025-12-30
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LimitPoint {

    /**
     * 资源名，无实际意义，可以用于排除异常
     */
    String name() default "";

    /**
     * 资源的key
     * <p>
     *  如果下方 limitType 值为自定义，那么全局限流参数来自于此
     *  如果limitType 为ip，则限流条件 为 key+ip
     */

    String key() default "";

    /**
     * Key的prefix redis 前缀，可选
     */
    String prefix() default "";

    /**
     * 给定时间段，单位秒
     */
    int period() default 60;

    /**
     * 最多访问限制次数
     */
    int limit() default 10;

    /**
     * 类型 ip限制 还是自定义key值限制
     * 建议使用ip，自定义key属于全局限制，ip则是某节点设置，通常情况使用IP
     */
    LimitTypeEnums limitType() default LimitTypeEnums.IP;
}
