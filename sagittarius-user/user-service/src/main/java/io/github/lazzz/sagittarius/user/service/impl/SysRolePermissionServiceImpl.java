package io.github.lazzz.sagittarius.user.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryTable;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.common.security.service.PermissionService;
import io.github.lazzz.sagittarius.common.constant.RedisConstants;
import io.github.lazzz.sagittarius.user.model.bo.RolePermsBO;
import io.github.lazzz.sagittarius.user.model.entity.SysPermission;
import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysRolePermissionService;
import io.github.lazzz.sagittarius.user.model.entity.SysRolePermission;
import io.github.lazzz.sagittarius.user.mapper.SysRolePermissionMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;
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
        if (!redisTemplate.hasKey(RedisConstants.ROLE_PERMS_PREFIX)){
            refreshRolePermsCache();
        }
    }

    @Override
    public void refreshRolePermsCache() {
        // 清理缓存
        redisTemplate.opsForHash().delete(RedisConstants.ROLE_PERMS_PREFIX, "*");
        var bo = queryChain().select("r.role_code", "p.perm_code as perms")
                .from(SysRole.class).as("r")
                .innerJoin(SysRolePermission.class).as("rp")
                .on(SysRole::getId, SysRolePermission::getRoleId)
                .innerJoin(SysPermission.class).as("p")
                .on(SysRolePermission::getPermissionId, SysPermission::getId)
                .listAs(RolePermsBO.class);
        if(CollectionUtil.isNotEmpty(bo)){
            bo.forEach(item -> {
                String roleCode = item.getRoleCode();
                Set<String> perms = item.getPerms();
                redisTemplate.opsForHash().put(RedisConstants.ROLE_PERMS_PREFIX, roleCode, perms);
            });
        }
    }

    @Override
    public void refreshRolePermsCache(String roleCode) {
        redisTemplate.opsForHash().delete(RedisConstants.ROLE_PERMS_PREFIX, roleCode);
        var bo = queryChain().select("r.role_code", "p.perm_code as perms")
                .from(SysRole.class).as("r")
                .innerJoin(SysRolePermission.class).as("rp")
                .on(SysRole::getId, SysRolePermission::getRoleId)
                .innerJoin(SysPermission.class).as("p")
                .on(SysRolePermission::getPermissionId, SysPermission::getId)
                .where(SysRole::getRoleCode).eq(roleCode)
                .oneAs(RolePermsBO.class);
        if(ObjectUtil.isNotNull(bo)){
            redisTemplate.opsForHash()
                    .put(RedisConstants.ROLE_PERMS_PREFIX,
                            bo.getRoleCode(),
                            bo.getPerms());
        }

    }

    @Override
    public void refreshRolePermsCache(String oldRoleCode, String newRoleCode) {
        redisTemplate.opsForHash().delete(RedisConstants.ROLE_PERMS_PREFIX, oldRoleCode);
        var bo = queryChain().select("r.role_code", "p.perm_code as perms")
                .from(SysRole.class).as("r")
                .innerJoin(SysRolePermission.class).as("rp")
                .on(SysRole::getId, SysRolePermission::getRoleId)
                .innerJoin(SysPermission.class).as("p")
                .on(SysRolePermission::getPermissionId, SysPermission::getId)
                .where(SysRole::getRoleCode).eq(newRoleCode)
                .oneAs(RolePermsBO.class);
        if(ObjectUtil.isNotNull(bo)){
            redisTemplate.opsForHash()
                    .put(RedisConstants.ROLE_PERMS_PREFIX,
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