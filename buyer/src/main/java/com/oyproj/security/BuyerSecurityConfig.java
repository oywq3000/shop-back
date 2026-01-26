package com.oyproj.security;

import com.oyproj.cache.Cache;
import com.oyproj.common.properties.IgnoredUrlsProperties;
import com.oyproj.common.security.CustomAccessDeniedHandler;
import com.oyproj.common.utils.SpringContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author oywq3000
 * @since 2026-01-26
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class BuyerSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 忽略验权配置
     */
    private final IgnoredUrlsProperties ignoredUrlsProperties;
    /**
     * spring security ->权限不足处理
     */
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final Cache<String> cache;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
        //配置的url不需要授权
        for(String url: ignoredUrlsProperties.getUrls()){
            registry.antMatchers(url).permitAll();
        }
        registry.and()
                //禁止网页iframe
                .headers().frameOptions().disable()
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                //任何请求
                .anyRequest()
                //需要身份认证
                .authenticated()
                .and()
                //允许跨域
                .cors().configurationSource((CorsConfigurationSource) SpringContextUtil.getBean("corsConfigurationSource")).and()
                //关闭跨站请求防护
                .csrf().disable()
                //前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //自定义权限拒绝处理类
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .addFilter(new BuyerAuthenticationFilter(authenticationManager(), cache));
    }
}
