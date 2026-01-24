package com.oyproj.common.vo;

import com.oyproj.common.utils.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jodd.util.CharSequenceUtil;
import org.apache.commons.text.CaseUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author oywq3000
 * @since 2026-01-23
 */
public class PageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "页号")
    private Integer pageNumber = 1;

    @Schema(description = "页面大小")
    private Integer pageSize = 10;

    @Schema(description = "排序字段")
    private String sort;

    @Schema(description = "排序方式 asc/desc")
    private String order;

    @Schema(description = "需要驼峰转换蛇形")
    private Boolean notConvert;
}
