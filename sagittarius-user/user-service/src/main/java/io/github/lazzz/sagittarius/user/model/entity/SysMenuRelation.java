package io.github.lazzz.sagittarius.user.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseMiddleEntity;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;

import java.lang.Long;
import java.sql.Timestamp;

/**
 * 菜单权限关联表 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "菜单权限关联表")
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_menu_relation")
public class SysMenuRelation extends BaseMiddleEntity {

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    @Column(value = "menu_id")
    private Long menuId;

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    @Column(value = "permission_id")
    private Long permissionId;

}
