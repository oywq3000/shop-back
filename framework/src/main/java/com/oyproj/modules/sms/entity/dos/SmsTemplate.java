package com.oyproj.modules.sms.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oyproj.mybatis.BaseIdEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.ScriptAssert;

/**
 * @author oywq3000
 * @since 2026-01-24
 */
@Data
@TableName("sms_template")
@Schema(description = "短息模板")
public class SmsTemplate extends BaseIdEntity {
    @Schema(description = "模板名称", required = true)
    private String templateName;

    @Schema(description = "短信类型", required = true)
    private Integer templateType;

    @Schema(description = "短信模板申请说明", required = true)
    private String remark;

    @Schema(description = "模板内容", required = true)
    private String templateContent;

    /**
     * 0：审核中。
     * 1：审核通过。
     * 2：审核失败，请在返回参数Reason中查看审核失败原因。
     */
    @Schema(description = "模板审核状态")
    private Integer templateStatus;

    @Schema(description = "短信模板CODE")
    private String templateCode;

    @Schema(description = "审核备注")
    private String reason;
}
