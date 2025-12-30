package com.oyproj.modules.verification.service;

import com.oyproj.cache.CachePrefix;
import com.oyproj.modules.verification.entity.dto.VerificationDTO;
import com.sun.javafx.binding.StringFormatter;

/**
 * @author oywq3000
 * @since 2025-12-30
 */
public interface VerificationSourceService {
    /**
     * 验证缓存前缀
     */
    String VERIFICATION_CACHE = CachePrefix.VERIFICATION.getPrefix();

    /**
     * 初始化缓存
     */
    VerificationDTO initCache();

    /**
     * 获取缓存
     *
     * @return 验证码
     */
    VerificationDTO getVerificationCache();



}
