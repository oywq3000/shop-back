package com.oyproj.common.security.context;

import com.oyproj.cache.Cache;
import com.oyproj.cache.CachePrefix;
import com.oyproj.common.enums.ResultCode;
import com.oyproj.common.exception.ServiceException;
import com.oyproj.common.utils.StringUtils;
import io.jsonwebtoken.Claims;
import com.oyproj.common.security.AuthUser;
import com.oyproj.common.security.enums.SecurityEnum;
import com.oyproj.common.security.token.SecretKeyUtil;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户上下文
 *
 * @author oywq3000
 * @since 2026-01-12
 */
public class UserContext {
    /**
     * 根据request获取用户信息
     */
    public static AuthUser getCurrentUser(){
        if(RequestContextHolder.getRequestAttributes()!=null){
            HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String accessToken = request.getHeader(SecurityEnum.HEADER_TOKEN.getValue());
            return getAuthUser(accessToken);
        }
        return null;
    }

    /**
     * 根据request获取用户信息
     * @return
     */
    public static String getUuid(){
        if(RequestContextHolder.getRequestAttributes()!=null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getHeader(SecurityEnum.UUID.getValue());
        }
        return null;
    }

    /**
     * 根据jwt获取token重的用户信息
     *
     * @param cache       缓存
     * @param accessToken token
     * @return 授权用户
     */
    public static AuthUser getAuthUser(Cache cache, String accessToken){
        try{
            AuthUser authUser = getAuthUser(accessToken);
            assert authUser!=null;
            if(!cache.hasKey(CachePrefix.ACCESS_TOKEN.getPrefix(authUser.getRole(),authUser.getId()))){
                throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
            }
            return authUser;
        }catch (Exception e){
            return null;
        }
    }

    public static String getCurrentUserToken(){
        if(RequestContextHolder.getRequestAttributes()!=null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getHeader(SecurityEnum.HEADER_TOKEN.getValue());
        }
        return null;
    }


    /**
     * 根据jwt获取token重的用户信息
     *
     * @param accessToken
     * @return
     */
    public static AuthUser getAuthUser(String accessToken){
        try {
            Claims claims = Jwts.parser()  // 替换 parserBuilder() 为 parser()
                    .setSigningKey(SecretKeyUtil.generalKeyByDecoders())
                    .parseClaimsJws(accessToken).getBody();// 直接设置签名密钥，无需 build()
            //获取存储在claims中的用户信息
            String json = claims.get(SecurityEnum.USER_CONTEXT.getValue()).toString();
            return new Gson().fromJson(json, AuthUser.class);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 写入邀人请信息
     */
    public static void settingInviter(String memberId, Cache cache){
        if (RequestContextHolder.getRequestAttributes() != null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //邀请人id
            String inviterId = request.getHeader(SecurityEnum.INVITER.getValue());
            if (StringUtils.isNotEmpty(inviterId)) {
                cache.put(CachePrefix.INVITER.getPrefix() + memberId, inviterId);
            }
        }
    }


}
