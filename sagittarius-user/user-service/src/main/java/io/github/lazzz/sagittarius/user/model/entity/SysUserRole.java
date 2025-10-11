package io.github.lazzz.sagittarius.user.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseMiddleEntity;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;

import java.lang.Long;

/**
 * 用户角色关联表 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "用户角色关联表")
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_user_role")
public class SysUserRole extends BaseMiddleEntity {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @Column(value = "user_id")
    private Long userId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    @Column(value = "role_id")
    private Long roleId;


}
