package io.github.lazzz.system.dto;

import lombok.Data;

import java.util.Set;

/**
 * 用户认证信息传输层对象
 *
 * @author Lazzz 
 * @date 2025/09/21 11:31
**/
@Data
public class UserAuthDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 用户类型（0-普通用户，1-博客作者，2-内容管理员）
     */
    private Integer userType;

    /**
     * 角色列表
     */
//    @RelationOneToMany(
//            selfField = "id",
//            targetField = "id",
//            targetTable = "sys_role",
//            valueField = "roleCode",
//            joinTable = "sys_user_role",
//            joinSelfColumn = "user_id",
//            joinTargetColumn = "role_id"
//    )
    private Set<String> roles;
}

