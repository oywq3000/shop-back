package com.oyproj.modules.mamber.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectQueryDTO {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 第三方id
     */
    private String unionId;
    /**
     * 联合登录类型
     */
    private String unionType;
}
