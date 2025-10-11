package io.github.lazzz.sagittarius.user.service;


import io.github.lazzz.sagittarius.user.model.entity.SysUserRole;
import com.mybatisflex.core.service.IService;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

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
     * @param roleId 角色ID
     * @return {@link Boolean}
     */
    boolean saveUserRole(
            @NotNull(message = "用户ID不能为空") Long userId,
            @NotNull(message = "角色ID不能为空") Long roleId);

    /**
     * 删除用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return {@link Boolean}
     */
    boolean deleteUserRole(
            @NotNull(message = "用户ID不能为空") Long userId,
            @NotNull(message = "角色ID不能为空") Long roleId);

    /**
     * 更新用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return {@link Boolean}
     */
    boolean updateUserRole(
            @NotNull(message = "用户ID不能为空") Long userId,
            @NotNull(message = "角色ID不能为空") Long roleId);

    /**
     * 判断角色是否被用户使用
     *
     * @param roleId 角色ID
     * @return {@link Boolean}
     */
    boolean hasAssignedUsers(@NotNull(message = "角色ID不能为空") Long roleId);

}