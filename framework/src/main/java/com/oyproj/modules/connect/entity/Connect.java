package com.oyproj.modules.connect.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oyproj.mybatis.BaseEntity;
import com.oyproj.mybatis.BaseIdEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("connet")
@Tag(name = "Connect",description = "联合登入")
@NoArgsConstructor
public class Connect extends BaseIdEntity {
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "获取校验接口,一分钟同一个ip请求10次",hidden = true)
    private String createBy;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "联合登入ID")
    private String unionId;

    @Schema(description = "联合登入类型")
    private String unionType;

    public Connect(String userId, String unionId, String unionType) {
        this.userId = userId;
        this.unionId = unionId;
        this.unionType = unionType;
    }
}
