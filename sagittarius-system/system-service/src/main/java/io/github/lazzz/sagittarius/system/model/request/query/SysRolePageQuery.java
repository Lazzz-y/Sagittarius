package io.github.lazzz.sagittarius.system.model.request.query;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.system.model.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 角色分页查询请求
 *
 * @className SysUserPageQuery
 * @author Lazzz
 * @date 2025/10/10 16:44
**/
@Data
public class SysRolePageQuery {

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
     * 角色编码
     */
    @Schema(description = "角色编码")
    private String roleCode;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 转换为分页对象
     *
     * @author Lazzz
     * @date 2025/10/10
     * @return {@link Page<SysRole>}
     */
    public Page<SysRole> toPage() {
        return new Page<>(pageNumber, pageSize);
    }

}

