package io.github.lazzz.sagittarius.user.model.entity;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

import java.lang.Long;
import java.util.Date;
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
public class SysUser {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @Id(keyType = KeyType.Auto)
    private Long id;

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
     * 邮箱
     */
    @Schema(description = "邮箱")
    @Column(value = "email")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @Column(value = "phone")
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
    @Column(value = "status")
    private Integer status;

    /**
     * 用户类型（0-普通用户，1-博客作者，2-内容管理员）
     */
    @Schema(description = "用户类型（0-普通用户，1-博客作者，2-内容管理员）")
    @Column(value = "user_type")
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
    @Column(value = "article_count")
    private Integer articleCount;

    /**
     * 评论数量
     */
    @Schema(description = "评论数量")
    @Column(value = "comment_count")
    private Integer commentCount;

    /**
     * 被访问次数
     */
    @Schema(description = "被访问次数")
    @Column(value = "view_count")
    private Integer viewCount;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Column(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Column(value = "update_time")
    private Date updateTime;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID")
    @Column(value = "create_by")
    private Long createBy;

    /**
     * 更新人ID
     */
    @Schema(description = "更新人ID")
    @Column(value = "update_by")
    private Long updateBy;

    /**
     * 逻辑删除标识(0:未删除;1:已删除)
     */
    @Schema(description = "逻辑删除标识(0:未删除;1:已删除)")
    @Column(value = "deleted")
    private Integer deleted;


}
