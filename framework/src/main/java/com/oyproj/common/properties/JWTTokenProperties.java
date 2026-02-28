package com.oyproj.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * token过期配置
 *
 * @author oywq3000
 * @since 2026-01-25
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shop.jwt-setting")
public class JWTTokenProperties {
    /**
     * token默认过期时间
     */
    private long tokenExpireTime = 60;
    /**
     * 最小token刷新间隔
     */
    private  long minRefreshInterval = 600000L;

}
