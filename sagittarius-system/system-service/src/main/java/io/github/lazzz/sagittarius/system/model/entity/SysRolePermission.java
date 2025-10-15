package io.github.lazzz.sagittarius.system.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseMiddleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;

import java.lang.Long;

import lombok.*;

/**
 * 角色权限关联表 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "角色权限关联表")
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_role_permission")
public class SysRolePermission extends BaseMiddleEntity {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    @Column(value = "role_id")
    private Long roleId;

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    @Column(value = "permission_id")
    private Long permissionId;

}
