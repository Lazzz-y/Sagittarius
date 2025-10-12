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
 * 系统用户详情服务
 *
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
        return new SysUserDetails(userAuthDTO);
    }
}

