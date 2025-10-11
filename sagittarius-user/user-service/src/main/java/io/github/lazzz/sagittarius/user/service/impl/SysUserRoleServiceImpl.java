package io.github.lazzz.sagittarius.user.service.impl;


import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import io.github.lazzz.sagittarius.user.model.entity.SysUser;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysUserRoleService;
import io.github.lazzz.sagittarius.user.model.entity.SysUserRole;
import io.github.lazzz.sagittarius.user.mapper.SysUserRoleMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户角色关联表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    @Transactional
    public boolean saveUserRole(Long userId, Long roleId) {
        return this.save(
                SysUserRole
                        .builder()
                        .userId(userId)
                        .roleId(roleId)
                        .build());
    }

    @Override
    @Transactional
    public boolean deleteUserRole(Long userId, Long roleId) {
        return this.remove(
                queryChain()
                        .eq(SysUserRole::getUserId, userId)
                        .eq(SysUserRole::getRoleId, roleId));
    }

    @Override
    @Transactional
    public boolean updateUserRole(Long userId, Long roleId) {
        return this.update(
                SysUserRole
                        .builder()
                        .userId(userId)
                        .roleId(roleId)
                        .build(),
                queryChain()
                        .eq(SysUserRole::getUserId, userId)
                        .eq(SysUserRole::getRoleId, roleId));
    }

    @Override
    public boolean hasAssignedUsers(Long roleId) {
        return this.count(queryChain()
                .from(SysUserRole.class)
                .innerJoin(SysUser.class)
                .on(SysUser::getId, SysUserRole::getUserId)
                .innerJoin(SysRole.class)
                .on(SysRole::getId, SysUserRole::getRoleId)
                .where(SysRole::getId).eq(roleId)) > 0;
    }
}