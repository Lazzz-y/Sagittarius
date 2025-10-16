package io.github.lazzz.sagittarius.system.model.request.query;

import io.github.lazzz.sagittarius.common.base.BasePageQuery;
import io.github.lazzz.sagittarius.system.model.entity.SysPermission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限分页查询请求
 *
 * @className SysUserPageQuery
 * @author Lazzz
 * @date 2025/10/10 16:44
**/
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPermPageQuery extends BasePageQuery<SysPermission> {

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

}

