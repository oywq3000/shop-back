package com.oyproj.modules.system.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oyproj.mybatis.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设置
 *
 * @author oywq3000
 * @since 2026-01-12
 */
@Data
@TableName("setting")
@Schema(description = "配置")
@NoArgsConstructor
public class Setting extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Schema(description = "配置值value")
    private String settingValue;
}
