package com.oyproj.modules.mamber.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.oyproj.common.security.sensitive.Sensitive;
import com.oyproj.common.security.sensitive.enums.SensitiveStrategy;
import com.oyproj.common.utils.CommonUtil;
import com.oyproj.mybatis.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 会员
 *
 * @author oywq3000
 * @since 2026-01-12
 */
@Data
@TableName("member")
@Schema(description = "会员")
@NoArgsConstructor
public class Member extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Schema(description = "会员名称")
    private String username;

    @Schema(description = "会员密码")
    private String password;

    @Schema(description = "昵称")
    private String nickName;

    @Min(message = "会员性别参数错误",value = 0)
    @Schema(description = "会员性别，1为男性，0为女性")
    private Integer sex;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "会员生日")
    private Date birthday;

    @Schema(description = "会员地址ID")
    private String regionId;

    @Schema(description = "会员地址")
    private String region;

    @NotEmpty(message = "手机号不能为空")
    @Schema(description = "手机号码",required = true)
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String mobile;

    @Min(message = "必须为数字",value = 0)
    @Schema(description = "积分数量")
    private Long point;

    @Min(message = "必须为数字", value = 0)
    @Schema(description = "总积分数")
    private Long totalPoint;

    @Schema(description = "会员头像")
    private String face;

    @Schema(description = "会员状态")
    private Boolean disabled;

    @Schema(description = "是否开通店铺")
    private Boolean haveStore;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "客户端")
    private String clientEnum;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后一次登录时间")
    private Date lastLoginDate;

    @Schema(description = "会员等级ID")
    private String gradeId;

    @Min(message = "必须为数字", value = 0)
    @Schema(description = "经验值数量")
    private Long experience;


    //新用户
    public Member(String username,String password,String mobile){
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.nickName = CommonUtil.getSpecialStr("用户");
        this.disabled = true;
        this.haveStore = false;
        this.sex = 0;
        this.point = 0L;
        this.totalPoint = 0L;
        this.lastLoginDate = new Date();
    }

    //老用户
    public Member(String username, String password, String face, String nickName, Integer sex,String mobile) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.nickName = nickName;
        this.disabled = true;
        this.haveStore = false;
        this.face = face;
        this.sex = sex;
        this.point = 0L;
        this.totalPoint = 0L;
        this.lastLoginDate = new Date();
    }
}
