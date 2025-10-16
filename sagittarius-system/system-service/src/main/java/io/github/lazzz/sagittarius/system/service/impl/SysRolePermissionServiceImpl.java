package io.github.lazzz.sagittarius.system.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.system.model.bo.SysRolePermsBO;
import io.github.lazzz.sagittarius.system.model.entity.SysPermission;
import io.github.lazzz.sagittarius.system.model.entity.SysRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.system.service.ISysRolePermissionService;
import io.github.lazzz.sagittarius.system.model.entity.SysRolePermission;
import io.github.lazzz.sagittarius.system.mapper.SysRolePermissionMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import java.util.Set;

/**
 * 角色权限关联表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void lazyLoadRole() {
        if (!redisTemplate.hasKey(CacheConstants.ROLE_PERMS_PREFIX)){
            refreshRolePermsCache();
        }
    }

    @Override
    public void refreshRolePermsCache() {
        // 清理缓存
        redisTemplate.opsForHash().delete(CacheConstants.ROLE_PERMS_PREFIX, "*");
        var bo = queryChain().select("r.role_code", "p.perm_code as perms")
                .from(SysRole.class).as("r")
                .innerJoin(SysRolePermission.class).as("rp")
                .on(SysRole::getId, SysRolePermission::getRoleId)
                .innerJoin(SysPermission.class).as("p")
                .on(SysRolePermission::getPermissionId, SysPermission::getId)
                .listAs(SysRolePermsBO.class);
        if(CollectionUtil.isNotEmpty(bo)){
            bo.forEach(item -> {
                String roleCode = item.getRoleCode();
                Set<String> perms = item.getPerms();
                redisTemplate.opsForHash().put(CacheConstants.ROLE_PERMS_PREFIX, roleCode, perms);
            });
        }
    }

    @Override
    public void refreshRolePermsCache(String roleCode) {
        redisTemplate.opsForHash().delete(CacheConstants.ROLE_PERMS_PREFIX, roleCode);
        var bo = queryChain().select("r.role_code", "p.perm_code as perms")
                .from(SysRole.class).as("r")
                .innerJoin(SysRolePermission.class).as("rp")
                .on(SysRole::getId, SysRolePermission::getRoleId)
                .innerJoin(SysPermission.class).as("p")
                .on(SysRolePermission::getPermissionId, SysPermission::getId)
                .where(SysRole::getRoleCode).eq(roleCode)
                .oneAs(SysRolePermsBO.class);
        if(ObjectUtil.isNotNull(bo)){
            redisTemplate.opsForHash()
                    .put(CacheConstants.ROLE_PERMS_PREFIX,
                            bo.getRoleCode(),
                            bo.getPerms());
        }

    }

    @Override
    public void refreshRolePermsCache(String oldRoleCode, String newRoleCode) {
        redisTemplate.opsForHash().delete(CacheConstants.ROLE_PERMS_PREFIX, oldRoleCode);
        var bo = queryChain().select("r.role_code", "p.perm_code as perms")
                .from(SysRole.class).as("r")
                .innerJoin(SysRolePermission.class).as("rp")
                .on(SysRole::getId, SysRolePermission::getRoleId)
                .innerJoin(SysPermission.class).as("p")
                .on(SysRolePermission::getPermissionId, SysPermission::getId)
                .where(SysRole::getRoleCode).eq(newRoleCode)
                .oneAs(SysRolePermsBO.class);
        if(ObjectUtil.isNotNull(bo)){
            redisTemplate.opsForHash()
                    .put(CacheConstants.ROLE_PERMS_PREFIX,
                            bo.getRoleCode(),
                            bo.getPerms());
        }
    }

    @Override
    public boolean hasAssignedRolesToPerms(Long permId) {
        return this.count(queryChain()
                .from(SysRolePermission.class)
                .innerJoin(SysRole.class)
                .on(SysRole::getId, SysRolePermission::getRoleId)
                .innerJoin(SysPermission.class)
                .on(SysRolePermission::getPermissionId, SysPermission::getId)
                .eq(SysPermission::getId, permId)) > 0;
    }
}