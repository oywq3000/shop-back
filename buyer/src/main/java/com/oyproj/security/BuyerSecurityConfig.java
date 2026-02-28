package com.oyproj.security;

import com.oyproj.cache.Cache;
import com.oyproj.common.properties.IgnoredUrlsProperties;
import com.oyproj.common.security.CustomAccessDeniedHandler;
import com.oyproj.common.utils.SpringContextUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
    private final CorsConfigurationSource corsConfigurationSource;
    private final Cache<String> cache;



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
        // 允许跨域（建议提前至此处）
        http.cors().configurationSource(corsConfigurationSource).and()
                // 关闭跨站请求防护
                .csrf().disable();
        //配置的url不需要授权
        for(String url: ignoredUrlsProperties.getUrls()){
            registry.antMatchers(url).permitAll();

        }
        registry.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 任何请求需要身份认证（注意：此规则必须在所有permitAll之后）
                .anyRequest().authenticated()
                .and()
                // 禁止网页iframe
                .headers().frameOptions().disable()
                .and()
                .logout()
                .permitAll()
                .and()
                // 前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 自定义权限拒绝处理类
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
               /* .oauth2Login() //配置Oauth登入
                .successHandler((request, response, authentication) -> {
                    // OAuth2 登录成功后的处理
                    // 对于前后端分离项目，可以返回 JSON 或重定向到前端页面
                    System.out.println("oauth2 authorization successfully!");
                    response.setStatus(HttpStatus.OK.value());
                    response.getWriter().write("{\"message\": \"OAuth2 login successful\"}");
                })
                .failureHandler((request, response, exception) -> {
                    // OAuth2 登录失败后的处理
                    System.out.println("oauth2 authorization failed!");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("{\"error\": \"OAuth2 login failed: " + exception.getMessage() + "\"}");
                }).and()*/
                .addFilterAfter(new BuyerAuthenticationFilter(cache), OAuth2LoginAuthenticationFilter.class);

        ; //配置Oauth登入
                // 允许跨域

        /*registry.antMatchers(HttpMethod.OPTIONS, "/**").permitAll().and()
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
                .cors().configurationSource(corsConfigurationSource).and()
                //关闭跨站请求防护
                .csrf().disable()
                //前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //自定义权限拒绝处理类
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .addFilter(new BuyerAuthenticationFilter(authenticationManager(), cache));*/
    }


}
