package io.github.lazzz.sagittarius.system.model.request.query;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.system.model.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户分页查询请求
 *
 * @className SysUserPageQuery
 * @author Lazzz 
 * @date 2025/09/26 23:15
**/
@Data
public class SysUserPageQuery {

    @Schema(description = "当前页码")
    @NotNull(message = "当前页码不能为空")
    private Long pageNumber;

    @Schema(description = "每页数量")
    @NotNull(message = "每页数量不能为空")
    private Long pageSize;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "状态")
    private Integer status;

    public Page<SysUser> toPage() {
        return new Page<>(pageNumber, pageSize);
    }

}

