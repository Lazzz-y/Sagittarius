package io.github.lazzz.sagittarius.user.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseSnowflakeEntity;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;

import java.lang.String;
import java.lang.Integer;

/**
 * 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "$table.comment")
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_role")
public class SysRole extends BaseSnowflakeEntity {

    /**
     * 角色编码: ROLE_ADMIN, ROLE_EDITOR, ROLE_USER
     */
    @Schema(description = "角色编码: ADMIN, EDITOR, USER")
    @Column(value = "role_code")
    private String roleCode;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    @Column(value = "role_name")
    private String roleName;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    @Column(value = "description")
    private String description;

    /**
     * 是否系统角色: 1-是, 0-否
     */
    @Schema(description = "是否系统角色: 1-是, 0-否")
    @Column(value = "is_system")
    private Integer isSystem;

}
