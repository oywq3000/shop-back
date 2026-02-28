package com.oyproj.common.security.token;

import com.google.gson.Gson;
import com.oyproj.cache.Cache;
import com.oyproj.cache.CachePrefix;
import com.oyproj.common.enums.ResultCode;
import com.oyproj.common.exception.ServiceException;
import com.oyproj.common.properties.JWTTokenProperties;
import com.oyproj.common.security.AuthUser;
import com.oyproj.common.security.enums.SecurityEnum;
import com.oyproj.common.security.enums.UserEnums;
import com.qiniu.util.Auth;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author oywq3000
 * @since 2026-01-25
 */
@Component
@RequiredArgsConstructor
public class TokenUtil {
    private final JWTTokenProperties tokenProperties;
    private final Cache cache;


    /**
     * 构建token
     */
    public Token createToken(AuthUser authUser){
        Token token = new Token();
        //访问token
        String accessToken = createToken(authUser,tokenProperties.getTokenExpireTime());
        cache.put(CachePrefix.ACCESS_TOKEN.getPrefix(authUser.getRole(), authUser.getId())+ accessToken,
                1,tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
        //刷新token生成策略：如果是长时间有效的token（用于app），则默认15天有效期刷新token。如果是普通用户登录，则刷新token为普通token2倍数
        Long expireTime = authUser.getLongTerm()?15*24*60L: tokenProperties.getTokenExpireTime() * 2;
        String refreshToken = createToken(authUser,expireTime);
        cache.put(CachePrefix.REFRESH_TOKEN.getPrefix(authUser.getRole(), authUser.getId()) + refreshToken, 1, expireTime, TimeUnit.MINUTES);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        return token;
    }

    /**
     * 刷新token
     */
    public Token refreshToken(String oldRefreshToken){
        Claims claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(SecretKeyUtil.generalKeyByDecoders())
                    .parseClaimsJws(oldRefreshToken).getBody();
        }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                IllegalArgumentException e){
            //token 过期 认证失败等
            throw new ServiceException(ResultCode.USER_AUTH_EXPIRED);
        }

        //获取存储在claims中的用户信息
        String json = claims.get(SecurityEnum.USER_CONTEXT.getValue()).toString();
        AuthUser authUser = new Gson().fromJson(json, AuthUser.class);
        UserEnums userEnums = authUser.getRole();
        //refresh token 频繁检测
        String refreshTimeKey =  CachePrefix.REFRESH_TOKEN_TIME.getPrefix(userEnums,authUser.getId());
        Long lastRefreshTime = (Long)cache.get(refreshTimeKey);
        long currentTime = System.currentTimeMillis();
        if(lastRefreshTime!=null
        &&tokenProperties.getMinRefreshInterval()>currentTime-lastRefreshTime){
            throw new ServiceException(ResultCode.TOKEN_REFRESH_TOO_FREQUENT);
        }
        //获取是否长期有效的token
        boolean longTerm = authUser.getLongTerm();
        //如果缓存中有刷新token
        if(cache.hasKey(CachePrefix.REFRESH_TOKEN.getPrefix(userEnums, authUser.getId()) + oldRefreshToken)){
            Token token = new Token();
            //访问token
            String accessToken = createToken(authUser,tokenProperties.getTokenExpireTime());
            cache.put(CachePrefix.ACCESS_TOKEN.getPrefix(userEnums, authUser.getId())+ accessToken,1
                    ,tokenProperties.getTokenExpireTime()
                    ,TimeUnit.MINUTES);

            //如果是信任登录设备，则刷新token长度继续延长
            Long expirationTime = tokenProperties.getTokenExpireTime()*2;
            if(longTerm){
                expirationTime = 60*24*15L;
                authUser.setLongTerm(true);
            }
            //刷新token生成策略：如果是长时间有效的token（用于app），则默认15天有效期刷新token。如果是普通用户登录，则刷新token为普通token2倍数
            String refreshToken = createToken(authUser, expirationTime);

            cache.put(CachePrefix.REFRESH_TOKEN.getPrefix(userEnums, authUser.getId()) + refreshToken, 1, expirationTime, TimeUnit.MINUTES);
            cache.remove(CachePrefix.REFRESH_TOKEN.getPrefix(userEnums, authUser.getId()) + oldRefreshToken);
            cache.put(refreshTimeKey, currentTime, expirationTime, TimeUnit.MINUTES);
            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);


            return token;
        }else {
            throw new ServiceException(ResultCode.USER_AUTH_EXPIRED);
        }
    }

    /**
     * 生成token
     */
    private String createToken(AuthUser authUser,Long expirationTime){
        //JWT 生成
        return Jwts.builder()
                //jwt 私有声明
                .claim(SecurityEnum.USER_CONTEXT.getValue(),new Gson().toJson(authUser))
                //jwt的主体
                .setSubject(authUser.getUsername())
                //生效时间
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 60 * 1000))
                //签名算法和密钥
                .signWith(SecretKeyUtil.generalKey())
                .compact();

    }

}
