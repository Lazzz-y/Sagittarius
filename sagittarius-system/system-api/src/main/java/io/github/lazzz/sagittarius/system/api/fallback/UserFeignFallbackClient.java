package io.github.lazzz.sagittarius.system.api.fallback;

import io.github.lazzz.sagittarius.system.dto.UserAuthDTO;
import io.github.lazzz.sagittarius.system.api.UserFeignClient;
import io.github.lazzz.sagittarius.system.dto.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 * 系统用户服务远程调用异常后的降级处理类
 *
 * @author Lazzz
 * @date 2025/10/16 13:44
 **/
@Slf4j
@Component
public class UserFeignFallbackClient implements UserFeignClient {

    @Override
    public UserAuthDTO getUserAuthDTO(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return new UserAuthDTO();
    }

    @Override
    public UserInfoDTO getUserInfoDTO(Serializable id) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return new UserInfoDTO();
    }
}
