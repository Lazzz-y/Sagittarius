package io.github.lazzz.sagittarius.user.model.request.form;


import cn.hutool.core.util.StrUtil;
import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.user.model.entity.SysUser;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Base64;

/**
 * 用户信息插入表单
 *
 * @author Lazzz
 * @date 2025/09/27 16:34
 **/
@Data
@AutoMapper(target = SysUser.class)
@Schema(description = "用户插入表单对象")
public class SysUserSaveForm {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @Pattern(regexp = "^$|^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @Pattern(regexp = "^$|^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String phone;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Schema(description = "状态（0-禁用，1-启用）")
    private Integer status;

    /**
     * 用户类型（0-普通用户，1-博客作者，2-内容管理员）
     */
    @Schema(description = "用户类型（0-普通用户，1-博客作者，2-内容管理员）")
    private Integer userType;

    /**
     * 个人简介
     */
    @Schema(description = "个人简介")
    private String bio;

    /**
     * 个人网站
     */
    @Schema(description = "个人网站")
    private String website;

    /**
     * 文章数量
     */
    @Schema(description = "文章数量")
    private Integer articleCount;

    /**
     * 评论数量
     */
    @Schema(description = "评论数量")
    private Integer commentCount;

    /**
     * 被访问次数
     */
    @Schema(description = "被访问次数")
    private Integer viewCount;

    /**
     * 更新人ID
     */
    @Schema(description = "更新人ID")
    private Long updateBy;

    public void init() {
        If.ifThen(
                StrUtil.isEmpty(this.nickname), () -> {
                    this.setNickname("用户-" + Base64.getEncoder().encodeToString(this.username.getBytes()));
                });
        If.ifThen(StrUtil.isEmpty(this.password), () -> this.setPassword(SystemConstants.DEFAULT_PASSWORD));
        If.ifThen(StrUtil.isEmpty(this.phone), () -> this.setPhone(null));
        If.ifThen(StrUtil.isEmpty(this.email), () -> this.setEmail(null));
    }
}

