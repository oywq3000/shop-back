package com.oyproj.modules.mamber.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author oywq3000
 * @since 2026-01-24
 */
@Data
public class QRCodeLoginSessionVO implements Serializable {
    private static final long serialVersionUID = 8793639296995408322L;
    private String token;
    private Integer status;
    private long duration;
    private long userId;
}
