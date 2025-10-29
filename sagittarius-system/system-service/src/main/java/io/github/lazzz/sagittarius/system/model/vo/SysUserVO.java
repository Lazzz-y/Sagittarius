package io.github.lazzz.sagittarius.system.model.vo;

import io.github.lazzz.sagittarius.common.base.BaseVO;
import io.github.lazzz.sagittarius.dict.annotation.Dict;
import io.github.lazzz.sagittarius.system.model.entity.SysUser;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 用户视图对象
 * @author Lazzz
 * @date 2025/09/26 21:06
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户视图对象")
@EqualsAndHashCode(callSuper = true)
@AutoMappers(value = {@AutoMapper(target = SysUser.class)})
public class SysUserVO extends BaseVO {

    /**
     * 主键ID
     */
    @Schema(description = "用户ID")
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 性别
     */
    @Dict(typeCode = "sex")
    @Schema(description = "性别")
    private Integer sex;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 用户类型（0-普通用户，1-博客作者，2-内容管理员）
     */
    @Schema(description = "用户类型")
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

}

