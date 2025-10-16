package io.github.lazzz.sagittarius.system.api;


import io.github.lazzz.sagittarius.common.web.config.FeignDecoderConfig;
import io.github.lazzz.sagittarius.system.api.fallback.DictFeignFallbackClient;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
//import io.github.lazzz.sagittarius.system.api.fallback.DictFeignFallbackClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 字典服务 Feign 接口
 *
 * @author Lazzz
 * @date 2025/10/15 20:55
 **/
@FeignClient(
        value = "sagittarius-system",
        contextId = "dict-service",
        path = "/api/v1/dict",
        configuration = {FeignDecoderConfig.class},
        fallback = DictFeignFallbackClient.class
)
public interface DictFeignClient {

    @GetMapping("/{typeCode}/dict")
    List<DictDetailDTO> getDictDetailDTO(@PathVariable("typeCode") String typeCode);

}

