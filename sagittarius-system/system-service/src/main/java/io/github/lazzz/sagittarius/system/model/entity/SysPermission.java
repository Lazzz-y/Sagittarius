package io.github.lazzz.sagittarius.system.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseSnowflakeLogicEntity;
import io.github.lazzz.sagittarius.system.model.vo.SysPermVO;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;

import java.lang.String;

/**
 * 系统权限表 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "系统权限表")
@Table(value = "sys_permission")
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysPermVO.class)
public class SysPermission extends BaseSnowflakeLogicEntity {

    /**
     * 权限编码
     */
    @Schema(description = "权限编码")
    @Column(value = "perm_code")
    private String permCode;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    @Column(value = "perm_name")
    private String permName;

    /**
     * 资源类型
     */
    @Schema(description = "资源类型")
    @Column(value = "resource_type")
    private String resourceType;

    /**
     * 资源ID, *表示所有
     */
    @Schema(description = "资源ID, *表示所有")
    @Column(value = "resource_id")
    private String resourceId;

    /**
     * 操作类型
     */
    @Schema(description = "操作类型")
    @Column(value = "action")
    private String action;

    /**
     * 权限描述
     */
    @Schema(description = "权限描述")
    @Column(value = "description")
    private String description;
}
