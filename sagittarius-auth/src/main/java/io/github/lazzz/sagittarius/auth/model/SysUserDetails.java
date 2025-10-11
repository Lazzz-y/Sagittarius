package io.github.lazzz.sagittarius.auth.model;


import cn.hutool.core.collection.CollectionUtil;
import io.github.lazzz.user.dto.UserAuthDTO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Lazzz
 * @className SysUserDetails
 * @description TODO
 * @date 2025/09/20 20:55
 **/
@Data
public class SysUserDetails implements UserDetails {


    private Long userId;
    private String username;
    private String password;
    private Boolean enabled;
    private Collection<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    public SysUserDetails(UserAuthDTO user) {
        this.setUserId(user.getId());
        this.setUsername(user.getUsername());
        this.setPassword("{bcrypt}" + user.getPassword());
        this.setEnabled(user.getStatus() == 1);
        if (CollectionUtil.isNotEmpty(user.getRoles())){
            authorities = user.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
    }

    public SysUserDetails(
            Long userId,
            String username,
            String password,
            Boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Set<? extends GrantedAuthority> authorities
    ) {
        Assert.isTrue(username != null && !username.isEmpty() && password != null,
                "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = Collections.unmodifiableSet(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // 账号是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账号是否未锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 密码是否未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账号是否可用
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}

