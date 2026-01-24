package com.oyproj.common.utils;

import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import java.util.Date;

/**
 * 雪花分布式id获取
 *
 * @author oywq3000
 * @since 2026-01-24
 */
@Slf4j
public class SnowFlake {
    private static Snowflake snowFlake;
    /**
     * 初始化配置
     */
    public static void initialize(long workerId,long datacenterId){
        snowFlake = IdUtil.getSnowflake(workerId,datacenterId);
    }

    public static long getId(){
        return snowFlake.nextId();
    }

    /**
     * 生成字符，带有前缀
     */
    public static String createStr(String prefix){
        return prefix+ DateUtil.toString(new Date(),"yyyyMMdd")+SnowFlake.getId();
    }
    public static String getIdStr(){
        return snowFlake.nextId()+"";
    }
}
