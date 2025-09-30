package io.github.lazzz.sagittarius.auth.model;

import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import lombok.Data;

/**
 * 验证码数据
 * 
 * @author Lazzz 
 * @date 2025/09/29 13:02
**/
@Data
public class CaptchaData {

    private String id;

    private ImageCaptchaTrack data;

}

