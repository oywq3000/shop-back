package com.oyproj.modules.sms.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oyproj.mybatis.BaseIdEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 短息签名
 *
 * @author oywq3000
 * @since 2026-01-24
 */
@Data
@TableName("sms_sign")
@Schema(description = "短息签名")
public class SmsSign extends BaseIdEntity {
    @Schema(description = "签名名称",required = true)
    private String signName;
    @Schema(description = "签名来源", required = true)
    private Integer signSource;

    @Schema(description = "短信签名申请说明", required = true)
    private String remark;

    @Schema(description = "营业执照", required = true)
    private String businessLicense;

    @Schema(description = "授权委托书", required = true)
    private String license;

    /**
     * 0：审核中。
     * 1：审核通过。
     * 2：审核失败，请在返回参数Reason中查看审核失败原因。
     */
    @Schema(description = "签名审核状态")
    private Integer signStatus;

    @Schema(description = "审核备注")
    private String reason;
}
