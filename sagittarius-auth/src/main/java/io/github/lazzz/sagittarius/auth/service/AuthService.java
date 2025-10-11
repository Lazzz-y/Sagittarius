package io.github.lazzz.sagittarius.auth.service;


import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.CaptchaResponse;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.response.ApiResponse;
import io.github.lazzz.sagittarius.auth.model.CaptchaData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 认证服务
 * 
 * @author Lazzz 
 * @date 2025/09/29 11:33
**/
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ImageCaptchaApplication application;

    @Value("${captcha.type}")
    private String captchaType;

    public CaptchaResponse<ImageCaptchaVO> generateCaptcha() {
        return application.generateCaptcha(captchaType);
    }

    public ApiResponse<?> checkCaptcha(CaptchaData data) {
        var matching = application.matching(data.getId(), data.getData());
        if (matching.isSuccess()) {
            return ApiResponse.ofSuccess(Collections.singletonMap("validToken", data.getId()));
        }
        return ApiResponse.ofError(matching.getMsg());
    }

}

