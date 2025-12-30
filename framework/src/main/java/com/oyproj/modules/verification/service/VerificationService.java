package com.oyproj.modules.verification.service;

import com.oyproj.modules.verification.entity.enums.VerificationEnum;

import java.util.Map;

/**
 * verification code service
 *
 * @author oywq3000
 * @since 2025-12-30
 */
public interface VerificationService {
    Map<String,Object> createVerification(VerificationEnum verificationEnum, String uuid);
}
