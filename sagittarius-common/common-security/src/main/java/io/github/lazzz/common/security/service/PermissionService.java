package io.github.lazzz.common.security.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import io.github.lazzz.common.security.util.SecurityUtils;
import io.github.lazzz.sagittarius.common.constant.RedisConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import java.util.*;

/**
 * SpringSecurity 权限校验
 *
 * @author Lazzz
 * @date 2025/09/25 15:59
 **/
@Slf4j
@Service("ss")
@RequiredArgsConstructor
public class PermissionService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 判断当前用户是否拥有指定权限
     *
     * @param requiredPerm 所需权限
     * @return 是否拥有权限
     */
    public boolean hasPerm(String requiredPerm) {
        if (StrUtil.isNotBlank(requiredPerm)) {
            return false;
        }
        if (SecurityUtils.isRoot()) {
            return true;
        }
        // 获取当前登录用户的角色编码集合
        Set<String> roleCodes = SecurityUtils.getRoles();
        if (CollectionUtil.isEmpty(roleCodes)) {
            return false;
        }
        // 获取当前登录用户的权限集合
        Set<String> rolePerms = this.getRolesPermsFormCache(roleCodes);
        if (CollectionUtil.isEmpty(rolePerms)) { return false; }
        // 判断当前登录用户的所有角色的权限列表中是否包含所需权限
        boolean hasPerm = rolePerms.stream()
                .anyMatch(rolePerm ->
                                // 匹配权限，支持通配符(* 等)
                                PatternMatchUtils.simpleMatch(rolePerm, requiredPerm)
                        );
        if (!hasPerm){ log.error("用户无操作权限"); }
        return hasPerm;
    }

    public Set<String> getRolesPermsFormCache(Set<String> roleCodes) {
        if (CollectionUtil.isEmpty(roleCodes)) {
            return Collections.emptySet();
        }
        Set<String> perms = new HashSet<>();
        // 从缓存中一次性获取所有角色的权限
        Collection<Object> roleCodesAsObjects = new ArrayList<>(roleCodes);
        List<Object> rolePermsList = redisTemplate.opsForHash().multiGet(RedisConstants.ROLE_PERMS_PREFIX, roleCodesAsObjects);
        for (Object rolePermsObj : rolePermsList) {
            if (rolePermsObj instanceof Set) {
                @SuppressWarnings("unchecked")
                Set<String> rolePerms = (Set<String>) rolePermsObj;
                perms.addAll(rolePerms);
            }
        }
        return perms;
    }
}

