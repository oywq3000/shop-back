package com.oyproj.controller;

import com.oyproj.cache.limit.annotation.LimitPoint;
import com.oyproj.common.vo.ResultMessage;
import com.oyproj.modules.verification.entity.enums.VerificationEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/common/common/slider")
@Tag(name = "SliderImageController",description = "滑块验证码接口")
public class SliderImageController {
    @LimitPoint(name = "slider_image",key = "verification")
    @GetMapping("/{verificationEnum}")
    @Operation(summary = "获取校验接口,一分钟同一个ip请求10次")
    public ResultMessage getSliderImage(@RequestHeader String uuid,@PathVariable VerificationEnum verificationEnum){

    }
}
