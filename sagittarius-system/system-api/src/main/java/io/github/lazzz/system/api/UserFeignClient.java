package io.github.lazzz.system.api;


import io.github.lazzz.common.web.config.FeignDecoderConfig;
import io.github.lazzz.system.dto.UserAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 用户服务 Feign 接口
 *
 * @author Lazzz
 * @date 2025/09/21 11:18
**/
@FeignClient(value = "sagittarius-system", configuration = {FeignDecoderConfig.class})
public interface UserFeignClient {
    @PostMapping("/api/v1/users/{username}/auth")
    UserAuthDTO getUserAuthDTO(@PathVariable String username);
}