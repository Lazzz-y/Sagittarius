package io.github.lazzz.sagittarius.user.service;


import io.github.lazzz.sagittarius.user.model.entity.SysUserRole;
import com.mybatisflex.core.service.IService;

/**
 * 用户角色关联表 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    boolean defaultRole();

}