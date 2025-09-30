package io.github.lazzz.sagittarius.auth.controller;

import cloud.tianai.captcha.application.vo.CaptchaResponse;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.response.ApiResponse;
import io.github.lazzz.sagittarius.auth.model.CaptchaData;
import io.github.lazzz.sagittarius.auth.service.AuthService;
import io.github.lazzz.sagittarius.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;


/**
 * 认证服务
 *
 * @author Lazzz 
 * @date 2025/09/19 20:15
**/
@RefreshScope
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "认证服务", description = "认证服务")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public String test() {
        return "Server is running";
    }

    @PostMapping("/captcha")
    public CaptchaResponse<ImageCaptchaVO> generateCaptcha() {
        System.out.println("生成验证码...");
        return authService.generateCaptcha();
    }

    @PostMapping("/verify")
    public ApiResponse<?> checkCaptcha(@RequestBody CaptchaData data) {
        return authService.checkCaptcha(data);
    }
}

