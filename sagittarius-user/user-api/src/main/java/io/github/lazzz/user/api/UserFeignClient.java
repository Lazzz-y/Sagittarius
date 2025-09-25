package io.github.lazzz.user.api;


import io.github.lazzz.common.web.config.FeignDecoderConfig;
import io.github.lazzz.user.dto.UserAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务 Feign 接口
 *
 * @author Lazzz
 * @date 2025/09/21 11:18
**/
@FeignClient(value = "sagittarius-user", configuration = {FeignDecoderConfig.class})
public interface UserFeignClient {
    @GetMapping("/api/v1/users/{username}/auth")
    UserAuthDTO getUserAuthDTO(@PathVariable String username);
}