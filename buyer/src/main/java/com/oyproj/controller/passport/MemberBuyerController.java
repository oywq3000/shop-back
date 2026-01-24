package com.oyproj.controller.passport;

import com.oyproj.common.enums.ResultCode;
import com.oyproj.common.enums.ResultUtil;
import com.oyproj.common.exception.ServiceException;
import com.oyproj.common.vo.ResultMessage;
import com.oyproj.modules.mamber.service.MemberService;
import com.oyproj.modules.sms.SmsUtil;
import com.oyproj.modules.verification.entity.enums.VerificationEnum;
import com.oyproj.modules.verification.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author oywq3000
 * @since 2026-01-24
 */
@Slf4j
@RestController
@Tag(description = "买家端，会员接口",name = "MemberBuyerController")
@RequestMapping("/buyer/passport/")
@RequiredArgsConstructor
public class MemberBuyerController {
    private final MemberService memberService;
    private final SmsUtil smsUtil;
    private final VerificationService verificationService;

    @Operation(summary = "注册新用户")
    @Parameters({
            @Parameter(),
    })
    @PostMapping("/register")
    public ResultMessage<Object> register(
            @NotNull(message = "用户名不能为空") @RequestParam String username,
            @NotNull(message = "密码不能为空") @RequestParam String password,
            @NotNull(message = "手机号为空") @RequestParam String mobilePhone,
            @RequestHeader String uuid,
            @NotNull(message = "验证码不能为空") @RequestParam String code
    ){
        if(smsUtil.verifyCode(mobilePhone, VerificationEnum.REGISTER,uuid,code)){
            return ResultUtil.data(memberService.register(username,password,mobilePhone));
        }else{
            throw new ServiceException(ResultCode.VERIFICATION_SMS_CHECKED_ERROR);
        }

    }


}
