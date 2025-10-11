package io.github.lazzz.sagittarius.user.service;


import io.github.lazzz.sagittarius.user.model.entity.SysUserRole;
import com.mybatisflex.core.service.IService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 用户角色关联表 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Validated
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 保存用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID
     * @return {@link Boolean}
     */
    boolean assignRoleToUser(
            @NotNull(message = "用户ID不能为空") Long userId,
            @NotEmpty(message = "角色ID不能为空") List<Long> roleIds);

    /**
     * 判断角色是否被用户使用
     *
     * @param roleId 角色ID
     * @return {@link Boolean}
     */
    boolean hasAssignedUsers(@NotNull(message = "角色ID不能为空") Long roleId);

}