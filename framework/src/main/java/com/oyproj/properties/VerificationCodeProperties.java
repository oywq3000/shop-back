package com.oyproj.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author oywq3000
 * @since 2025-12-30
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shop.verification-code")
public class VerificationCodeProperties {
    /**
     * 过期时间
     * 包含滑块验证码有效时间，以及验证通过之后，缓存中存储的验证结果有效时间
     */
    private Long effectiveTime = 600L;

    /**
     * 水印
     */
    private String watermark = "";

    /**
     * 干扰数量 最大数量
     */
    private Integer interfereNum = 0;

    /**
     * 容错像素
     */
    private Integer faultTolerant = 3;

    public String getWatermark() {
        return watermark;
    }

    public Integer getInterfereNum(){
        if(interfereNum>2){
            return 2;
        }
        return interfereNum;
    }
}
