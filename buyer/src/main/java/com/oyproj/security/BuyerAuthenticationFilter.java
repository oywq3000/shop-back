package com.oyproj.security;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.oyproj.cache.Cache;
import com.oyproj.cache.CachePrefix;
import com.oyproj.common.enums.ResultCode;
import com.oyproj.common.exception.ServiceException;
import com.oyproj.common.security.AuthUser;
import com.oyproj.common.security.enums.SecurityEnum;
import com.oyproj.common.security.enums.UserEnums;
import com.oyproj.common.security.token.SecretKeyUtil;
import com.oyproj.common.utils.ResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 认证结果过滤器
 *
 * @author oywq3000
 * @since 2026-01-26
 */

@Slf4j
public class BuyerAuthenticationFilter extends OncePerRequestFilter {
    /**
     * 缓存
     */

    private Cache cache;
    /**
     * 自定义构造器
     *
     *
     */
    public BuyerAuthenticationFilter(Cache cache) {
        this.cache = cache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //从header中获取
        //从header中获取jwt
        String jwt = request.getHeader(SecurityEnum.HEADER_TOKEN.getValue());
        try{
            //如果没有token则return
            if(StrUtil.isBlank(jwt)){
                chain.doFilter(request, response);
                return;
            }
            //获取用户信息，存入context
            UsernamePasswordAuthenticationToken authentication = getAuthentication(jwt, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            log.error("BuyerAuthenticationFilter-> member authentication exception:", e);
            throw new ServiceException(ResultCode.USER_AUTH_EXPIRED);
        }
        chain.doFilter(request, response);
    }

    /**
     * 解析用户
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String jwt, HttpServletResponse response){
        try{
            Claims claims
                    = Jwts.parser()
                    .setSigningKey(SecretKeyUtil.generalKeyByDecoders())
                    .parseClaimsJws(jwt).getBody();
            //获取存储在claims中的用户信息
            String json = claims.get(SecurityEnum.USER_CONTEXT.getValue()).toString();
            AuthUser authUser = new Gson().fromJson(json, AuthUser.class);
            //校验redis中是否有权限
            //校验redis中是否存有该{用户类型,用户ID}_jwtToken
            if(cache.hasKey(CachePrefix.ACCESS_TOKEN.getPrefix(UserEnums.MEMBER,authUser.getId())+ jwt)){
                //构造返回信息
                List<GrantedAuthority> auths = new ArrayList<>();
                auths.add(new SimpleGrantedAuthority("ROLE_" + authUser.getRole().name()));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser.getUsername(), null, auths);
                authentication.setDetails(authUser);
                return authentication;
            }
            ResponseUtil.output(response, 403, ResponseUtil.resultMap(false, 403, "登录已失效，请重新登录"));
            return null;
        }catch (ExpiredJwtException e){
            log.debug("user analysis exception:", e);
        }catch (Exception e) {
            log.error("user analysis exception:", e);
        }
        return null;
    }
}
