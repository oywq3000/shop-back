package com.oyproj.modules.mamber.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author oywq3000
 * @since 2026-01-23
 */
@Data
public class MemberEditDTO {
    @Schema(description = "昵称",required = true)
    @Size(min = 2,max = 20,message = "会员昵称必须为2到20位之间")
    private String nickName;

    @Schema(description = "会员地址ID")
    private String regionId;

    @Schema(description = "会员地址")
    private String region;
    @Min(message = "必须为数字且1为男,0为女", value = 0)
    @Max(message = "必须为数字且1为男,0为女", value = 1)
    @NotNull(message = "会员性别不能为空")
    @Schema(description = "会员性别，1为男性，2为女性",required = true)
    private Integer sex;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "会员生日")
    private Date birthday;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "会员头像")
    private String face;
}
