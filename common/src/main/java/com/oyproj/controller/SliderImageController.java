package com.oyproj.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/common/common/slider")
@Tag(name = "SliderImageController",description = "滑块验证码接口")
public class SliderImageController {

}
