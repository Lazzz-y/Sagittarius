package io.github.lazzz.sagittarius.user.model.vo;


import io.github.lazzz.sagittarius.common.base.BaseVO;
import io.github.lazzz.sagittarius.user.model.entity.SysPermission;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 角色视图对象
 *
 * @author Lazzz
 * @date 2025/10/12 20:07
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "权限视图对象")
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysPermission.class)
public class SysPermVO extends BaseVO {

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private Long id;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码")
    private String permCode;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    private String permName;

    /**
     * 资源类型
     */
    @Schema(description = "资源类型")
    private String resourceType;

    /**
     * 资源ID, *表示所有
     */
    @Schema(description = "资源ID, *表示所有")
    private String resourceId;

    /**
     * 操作类型
     */
    @Schema(description = "操作类型")
    private String action;

    /**
     * 权限描述
     */
    @Schema(description = "权限描述")
    private String description;
}

