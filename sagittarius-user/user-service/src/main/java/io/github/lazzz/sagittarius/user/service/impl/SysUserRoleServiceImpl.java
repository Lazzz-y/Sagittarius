package io.github.lazzz.sagittarius.user.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.common.utils.condition.IfFlattener;
import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import io.github.lazzz.sagittarius.user.model.entity.SysUser;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysUserRoleService;
import io.github.lazzz.sagittarius.user.model.entity.SysUserRole;
import io.github.lazzz.sagittarius.user.mapper.SysUserRoleMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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
    public boolean assignRoleToUser(Long userId, List<Long> roleIds) {
        // 用户原角色ID集合
        List<Long> userRoleIds = this.list(
                queryChain().eq(SysUserRole::getUserId, userId))
                .stream().map(SysUserRole::getRoleId).toList();
        // 新增角色Id集合
        List<Long> saveRoleIds;
        if (CollectionUtil.isEmpty(userRoleIds)) {
            saveRoleIds = roleIds;
        } else {
            // userRoleIds = [1,2,3,4]
            // roleIds = [1,2,5,6]
            // saveRoleIds = [5,6]
            saveRoleIds = roleIds.stream()
                    // 从目标角色Id集合中过滤掉原角色Id集合中不包含的角色id
                    .filter(id -> !userRoleIds.contains(id))
                    .toList();
        }
        // 构建新增用户角色关系对象
        List<SysUserRole> saveUserRoles = saveRoleIds.stream()
                .map(id -> new SysUserRole(userId, id))
                .toList();
        this.saveBatch(saveUserRoles);

        // 删除用户角色关系
        if (CollectionUtil.isNotEmpty(userRoleIds)) {
            // userRoleIds = [1,2,3,4]
            // roleIds = [1,2,5,6]
            // deleteRoleIds = [3,4]
            List<Long> deleteRoleIds = userRoleIds.stream()
                    // 从原角色中过滤目标角色中不存在的角色id
                    .filter(id -> !roleIds.contains(id))
                    .toList();
            if (CollectionUtil.isNotEmpty(deleteRoleIds)){
                this.remove(queryChain()
                        .eq(SysUserRole::getUserId, userId)
                        .in(SysUserRole::getRoleId, deleteRoleIds)
                );
            }
        }
        return true;
    }

    @Override
    public boolean hasAssignedUsersToRoles(Long roleId) {
        return this.count(queryChain()
                .from(SysUserRole.class)
                .innerJoin(SysUser.class)
                .on(SysUser::getId, SysUserRole::getUserId)
                .innerJoin(SysRole.class)
                .on(SysRole::getId, SysUserRole::getRoleId)
                .where(SysRole::getId).eq(roleId)) > 0;
    }
}