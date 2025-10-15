package io.github.lazzz.sagittarius.system.service;


import io.github.lazzz.sagittarius.system.model.entity.SysRolePermission;
import com.mybatisflex.core.service.IService;

/**
 * 角色权限关联表 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    void lazyLoadRole();

    /**
     * 刷新角色权限缓存
     */
    void refreshRolePermsCache();

    /**
     * 刷新权限缓存(指定角色)
     *
     * @param roleCode 角色编码
     */
    void refreshRolePermsCache(String roleCode);

    /**
     * 刷新权限缓存(修改角色编码时调用)
     *
     * @param oldRoleCode 旧角色编码
     * @param newRoleCode 新角色编码
     */
    void refreshRolePermsCache(String oldRoleCode, String newRoleCode);

    /**
     * 判断角色是否被权限关联
     *
     * @param permId 角色ID
     * @return {@link Boolean}
     */
    boolean hasAssignedRolesToPerms(Long permId);
}