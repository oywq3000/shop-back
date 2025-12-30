package com.oyproj.modules.verification.entity.dto;

import com.oyproj.modules.verification.entity.dos.VerificationSource;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 验证码资源缓存DTO
 *
 * @author oywq3000
 * @since 2025-12-30
 */
@Data
public class VerificationDTO implements Serializable {
    /**
     * 缓存资源
     */
    List<VerificationSource> verificationResources;

    /**
     * 缓存滑块资源
     */
    List<VerificationSource> verificationSliders;
}
