package com.oyproj.modules.mamber.entity.vo;

import com.oyproj.common.security.token.Token;
import lombok.Data;

/**
 * @author oywq3000
 * @since 2026-01-24
 */
@Data
public class QRLoginResultVO {
    private Token token;

    private int status;
}
