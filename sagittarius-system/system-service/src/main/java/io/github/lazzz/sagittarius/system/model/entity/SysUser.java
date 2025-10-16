package io.github.lazzz.sagittarius.system.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.mask.Masks;
import io.github.lazzz.sagittarius.common.base.BaseSnowflakeLogicEntity;
import io.github.lazzz.sagittarius.jetcache.annotation.Dict;
import io.github.lazzz.sagittarius.system.model.vo.SysUserVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.lang.Integer;

/**
 * 用户表 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "用户表")
@Table(value = "sys_user")
@EqualsAndHashCode(callSuper = true)
@AutoMappers(value = {@AutoMapper(target = SysUserVO.class)})
public class SysUser extends BaseSnowflakeLogicEntity {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @Column(value = "username")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    @Column(value = "nickname")
    private String nickname;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @Column(value = "password")
    private String password;

    /**
     * 性别
     */
    @Schema(description = "性别（0-未知，1-男，2-女）")
    @Column(value = "sex", onInsertValue = "0")
    private Integer sex;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @Column(value = "email")
    @ColumnMask(Masks.EMAIL)
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @Column(value = "phone")
    @ColumnMask(Masks.FIXED_PHONE)
    private String phone;

    /**
     * 头像
     */
    @Schema(description = "头像")
    @Column(value = "avatar")
    private String avatar;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Schema(description = "状态（0-禁用，1-启用）")
    @Column(value = "status", onInsertValue = "1")
    private Integer status;

    /**
     * 用户类型（0-普通用户，1-博客作者，2-内容管理员）
     */
    @Schema(description = "用户类型（0-普通用户，1-博客作者，2-内容管理员）")
    @Column(value = "user_type", onInsertValue = "0")
    private Integer userType;

    /**
     * 个人简介
     */
    @Schema(description = "个人简介")
    @Column(value = "bio")
    private String bio;

    /**
     * 个人网站
     */
    @Schema(description = "个人网站")
    @Column(value = "website")
    private String website;

    /**
     * 文章数量
     */
    @Schema(description = "文章数量")
    @Column(value = "article_count", onInsertValue = "0")
    private Integer articleCount;

    /**
     * 评论数量
     */
    @Schema(description = "评论数量")
    @Column(value = "comment_count", onInsertValue = "0")
    private Integer commentCount;

    /**
     * 被访问次数
     */
    @Schema(description = "被访问次数")
    @Column(value = "view_count", onInsertValue = "0")
    private Integer viewCount;

}
