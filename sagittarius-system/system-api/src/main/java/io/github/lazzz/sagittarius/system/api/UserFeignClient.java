package io.github.lazzz.sagittarius.system.api;


import io.github.lazzz.sagittarius.common.web.config.FeignDecoderConfig;
import io.github.lazzz.sagittarius.system.dto.UserAuthDTO;
import io.github.lazzz.sagittarius.system.api.fallback.UserFeignFallbackClient;
import io.github.lazzz.sagittarius.system.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.Serializable;

/**
 * 用户服务 Feign 接口
 *
 * @author Lazzz
 * @date 2025/09/21 11:18
 **/
@FeignClient(
        value = "sagittarius-system",
        contextId = "user-service",
        path = "/api/v1/users",
        configuration = {FeignDecoderConfig.class},
        fallback = UserFeignFallbackClient.class
)
public interface UserFeignClient {
    @PostMapping("/{username}/auth")
    UserAuthDTO getUserAuthDTO(@PathVariable String username);

    @GetMapping("/{id}/userInfoDTO")
    UserInfoDTO getUserInfoDTO(@PathVariable Serializable id);
}