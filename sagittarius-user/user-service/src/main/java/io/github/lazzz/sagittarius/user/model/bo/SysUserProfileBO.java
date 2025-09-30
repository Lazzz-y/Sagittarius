package io.github.lazzz.sagittarius.user.model.bo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lazzz.sagittarius.user.model.vo.SysUserProfileVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 个人用户信息
 * 
 * @author Lazzz 
 * @date 2025/09/29 19:49
**/
@Data
@AutoMapper(target = SysUserProfileVO.class)
public class SysUserProfileBO {

    /**
     * ID
     */
    @Schema(description = "ID")
    private Long id;

    /**
     * 登录账号
     */
    @Schema(description = "登录账号")
    private String username;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;

    @AutoMapping(
            targetClass = SysUserProfileVO.class,
            target = "sex",
            expression =
                    """
                    java(io.github.lazzz.sagittarius.common.base.IBaseEnum.getLabelByValue(source.getSex()
                    , io.github.lazzz.sagittarius.common.enums.SexEnum.class))
                    """
    )
    @Schema(description = "性别")
    private Integer sex;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    private String phone;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 用户类型（0-普通用户，1-博客作者，2-内容管理员）
     */
    @AutoMapping(
            targetClass = SysUserProfileVO.class,
            target = "userType",
            expression =
                    """
                    java(io.github.lazzz.sagittarius.common.base.IBaseEnum.getLabelByValue(source.getUserType()
                    , io.github.lazzz.sagittarius.common.enums.UserTypeEnum.class))
                    """
    )
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
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}

