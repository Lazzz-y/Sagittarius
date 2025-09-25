package io.github.lazzz.sagittarius.auth.service;


import cn.hutool.core.lang.Assert;
import io.github.lazzz.sagittarius.auth.model.SysUserDetails;
import io.github.lazzz.user.api.UserFeignClient;
import io.github.lazzz.user.dto.UserAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * @className SysUserDetailsService
 * @description TODO
 * @author Lazzz
 * @date 2025/09/21 13:48
**/
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {

    private final UserFeignClient userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAuthDTO userAuthDTO = userFeignClient.getUserAuthDTO(username);
        Assert.isTrue(userAuthDTO != null, "用户不存在");
        if (!userAuthDTO.getStatus().equals(1)) {
            throw new DisabledException("用户被禁用");
        }
        log.info("Found user in database:");
        log.info("  ID: {}", userAuthDTO.getId());
        log.info("  Username: {}", userAuthDTO.getUsername());
        log.info("  Status: {}", userAuthDTO.getStatus());
        log.info("  Password (first 20 chars): {}",
                userAuthDTO.getPassword() != null && userAuthDTO.getPassword().length() > 20 ?
                        userAuthDTO.getPassword().substring(0, 20) + "..." : userAuthDTO.getPassword());
        return new SysUserDetails(userAuthDTO);
    }
}

