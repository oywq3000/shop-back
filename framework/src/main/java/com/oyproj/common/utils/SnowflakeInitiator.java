package com.oyproj.common.utils;

import com.oyproj.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * SnowflakeInitiator
 *
 * @author oywq3000
 * @since 2026-01-27
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SnowflakeInitiator {
    /**
     * 存储前缀
     */
    private static final String KEY = "{Snowflake}";
    private final Cache cache;

    /**
     * 尝试初始化
     *
     */
    @PostConstruct
    public void init(){
        Long num = cache.incr(KEY);
        long dataCenter = num / 32;
        long workedId = num % 32;
        //如果数据中心大于32，则抹除缓存，从头开始
        if(dataCenter>=32){
            cache.remove(KEY);
            num = cache.incr(KEY);
            dataCenter = num/32;
            workedId = num%32;
        }
        SnowFlake.initialize(workedId, dataCenter);
    }
    public static void main(String[] args) {
        SnowFlake.initialize(0, 8);
        System.out.println(SnowFlake.getId());
    }
}
