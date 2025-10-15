package io.github.lazzz.sagittarius.system.model.request.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户注册表单
 *
 * @author Lazzz
 * @date 2025/10/12 19:41
 **/
@Schema(description = "用户注册表单")
@Data
public class SysUserRegisterForm {

    @Schema(description="登录账号")
    @NotBlank(message = "登录账号不能为空")
    private String username;

    @Schema(description="手机号码")
    @Pattern(regexp = "^$|^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String phone;

    @Schema(description="密码")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,20}$", message = "密码至少包含数字,英文,字符中的两种以上，长度8-20")
    private String password;

    @Schema(description="验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;

}
