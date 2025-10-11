package io.github.lazzz.sagittarius.user.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseSnowflakeEntity;
import io.github.lazzz.sagittarius.user.model.vo.SysRoleVO;
import io.github.linpeilie.annotations.AutoMapper;
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
@AutoMapper(target = SysRoleVO.class)
public class SysRole extends BaseSnowflakeEntity {

    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
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
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    @Column(value = "sort")
    private Integer sort;

}
