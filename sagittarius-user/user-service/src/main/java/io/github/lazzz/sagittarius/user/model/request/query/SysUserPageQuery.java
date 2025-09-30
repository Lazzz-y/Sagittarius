package io.github.lazzz.sagittarius.user.model.request.query;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.user.model.entity.SysUser;
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

    @Schema(description = "页码")
    @NotNull
    private Long pageNumber;

    @Schema(description = "每页数量")
    @NotNull
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

