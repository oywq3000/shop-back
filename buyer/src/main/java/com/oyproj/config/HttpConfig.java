package com.oyproj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP配置类
 * 配置RestTemplate等HTTP相关组件
 * 
 * @author oywq3000
 * @since 2026-01-24
 */
@Configuration
public class HttpConfig {
    
    /**
     * 配置RestTemplate bean
     * 用于HTTP请求，如OAuth2认证、第三方API调用等
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }
    
    /**
     * 配置HTTP请求工厂
     * 设置连接超时和读取超时时间
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时时间（毫秒）
        factory.setConnectTimeout(5000);
        // 读取超时时间（毫秒）
        factory.setReadTimeout(10000);
        return factory;
    }
}
