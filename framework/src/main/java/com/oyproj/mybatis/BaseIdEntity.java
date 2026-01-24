package com.oyproj.mybatis;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据库基础实体类
 *
 * @author oywq3000
 * @since 2026-01-24
 */
@Data
@JsonIgnoreProperties(value = {"handler","fieldHandler"})
@AllArgsConstructor
@NoArgsConstructor
public class BaseIdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @Schema(description = "唯一标识")
    private String id;
}
