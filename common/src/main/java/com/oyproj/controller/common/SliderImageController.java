package com.oyproj.controller.common;

import com.oyproj.cache.limit.annotation.LimitPoint;
import com.oyproj.common.enums.ResultUtil;
import com.oyproj.common.vo.ResultMessage;
import com.oyproj.modules.verification.entity.enums.VerificationEnum;
import com.oyproj.modules.verification.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/common/common/slider")
@Tag(name = "SliderImageController",description = "滑块验证码接口")
@RequiredArgsConstructor
public class SliderImageController {

    private final VerificationService verificationService;



    @GetMapping("/test")
    @Operation(summary = "测试")
    public ResultMessage getSliderImage1(){
        return ResultUtil.success();
    }

    @LimitPoint(name = "slider_image",key = "verification")
    @GetMapping("/{verificationEnum}")
    @Operation(summary = "获取校验接口,一分钟同一个ip请求10次")
    public ResultMessage getSliderImage(@RequestHeader String uuid,@PathVariable VerificationEnum verificationEnum){
        return ResultUtil.data(verificationService.createVerification(verificationEnum,uuid));
    }

    @LimitPoint(name="slider_image",key = "verification_pre_check",limit = 600)
    @PostMapping("/{verificationEnum}")
    @Operation(summary = "验证校验接口")
    public ResultMessage verifySliderImage(Integer xPos, @RequestHeader String uuid
            ,@PathVariable VerificationEnum verificationEnum){
        return ResultUtil.data(verificationService.preCheck(xPos,verificationEnum,uuid));
    }
}
