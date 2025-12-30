package com.oyproj.modules.verification.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oyproj.modules.verification.entity.enums.VerificationSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author oywq3000
 * @since 2025-12-30
 */
@Data
@TableName("verification_source")
@Schema(description = "验证码资源维护")
public class VerificationSource {
    private static final long serialVersionUID = 1L;

    @Schema(description = "资源名称")
    private String name;

    @Schema(description = "资源地址")
    private String resource;

    /**
     * @see VerificationSourceEnum
     */
    @Schema(description = "验证码资源类型 SLIDER/SOURCE")
    private String type;
}
