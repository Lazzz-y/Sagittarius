package io.github.lazzz.common.security.util;


import cn.hutool.core.convert.Convert;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Security 工具类
 * @author Lazzz 
 * @date 2025/09/22 20:29
**/
public class SecurityUtils {

    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    public static Long getUserId() {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toLong(tokenAttributes.get("userId"));
        }
        return null;
    }

    /**
     * 获取当前登录用户名
     * @return 用户名
     */
    public static String getUsername() {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toStr(tokenAttributes.get("username"));
        }
        return null;
    }

    // 获取当前登录用户的jti
    public static String getJti(){
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toStr(tokenAttributes.get("jti"));
        }
        return null;
    }

    public static Long getExp() {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toLong(tokenAttributes.get("exp"));
        }
        return null;
    }

    /**
     * 获取数据权限范围
     *
     * @return 权限范围
     */
    public static Integer getDataScope() {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toInt(tokenAttributes.get("dataScope"));
        }
        return null;
    }

    public static Set<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return AuthorityUtils.authorityListToSet(authentication.getAuthorities())
                    .stream()
                    .collect(
                            Collectors.collectingAndThen(Collectors.toSet(),
                                    Collections::unmodifiableSet)
                    );
        }
        return null;
    }

    /**
     * 判断当前用户是否是超级管理员
     *
     * @return 是否是超级管理员
     */
    public static boolean isRoot() {
        Set<String> roles = getRoles();
        return roles != null && roles.contains("ROLE_ROOT");
    }

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    public static Map<String, Object> getTokenAttributes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getTokenAttributes();
        }
        return null;
    }

}

