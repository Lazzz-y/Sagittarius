package io.github.lazzz.sagittarius.user.model.request.query;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.user.model.entity.SysPermission;
import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 权限分页查询请求
 *
 * @className SysUserPageQuery
 * @author Lazzz
 * @date 2025/10/10 16:44
**/
@Data
public class SysPermPageQuery {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    @NotNull(message = "当前页码不能为空")
    private Long pageNumber;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量")
    @NotNull(message = "每页数量不能为空")
    private Long pageSize;

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
     * 转换为分页对象
     *
     * @author Lazzz
     * @date 2025/10/10
     * @return {@link Page<SysPermission>}
     */
    public Page<SysPermission> toPage() {
        return new Page<>(pageNumber, pageSize);
    }

}

