package com.oyproj.common.security.token;
import javax.crypto.SecretKey;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;

/**
 * @author oywq3000
 * @since 2026-01-12
 */
public class SecretKeyUtil {
    public static SecretKey generalKey(){
        //自定义
        byte[] encodedKey = Base64.decodeBase64("cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ=");
        SecretKey key = Keys.hmacShaKeyFor(encodedKey);
        return key;
    }

    public static SecretKey generalKeyByDecoders(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode("cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ="));
    }
}
