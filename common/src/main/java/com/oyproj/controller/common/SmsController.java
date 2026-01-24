package com.oyproj.controller.common;

import com.oyproj.cache.limit.annotation.LimitPoint;
import com.oyproj.common.enums.ResultCode;
import com.oyproj.common.enums.ResultUtil;
import com.oyproj.common.vo.ResultMessage;
import com.oyproj.modules.sms.SmsUtil;
import com.oyproj.modules.verification.entity.enums.VerificationEnum;
import com.oyproj.modules.verification.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 短息验证码接口
 *
 * @author oywq3000
 * @since 2026-01-11
 */
@RestController
@Tag(name = "SmsController",description = "短息验证码接口")
@RequestMapping("/common/common/sms")
@RequiredArgsConstructor
public class SmsController {
    private final VerificationService verificationService;
    private final SmsUtil smsUtil;

    @LimitPoint(name = "sms_send",key = "sms")
    @Parameters({
            @Parameter(in = ParameterIn.PATH, name = "mobile", description = "手机号", required = true),
            @Parameter(in = ParameterIn.HEADER, name = "uuid", description = "uuid", required = true),
            @Parameter(in = ParameterIn.PATH, name = "verificationEnums", description = "验证码类型", required = true)
    })
    @GetMapping("/{verificationEnums}/{mobile}")
    @Operation(description = "发送短息验证码，一分钟同一ip只能请求一次")
    public ResultMessage getSmsCode(@RequestHeader String uuid,
                                    @PathVariable String mobile,
                                    @PathVariable VerificationEnum verificationEnum){
        verificationService.check(uuid,verificationEnum);
        smsUtil.sendSmsCode(mobile, verificationEnum, uuid);
        return ResultUtil.success(ResultCode.VERIFICATION_SEND_SUCCESS);
    }
}
