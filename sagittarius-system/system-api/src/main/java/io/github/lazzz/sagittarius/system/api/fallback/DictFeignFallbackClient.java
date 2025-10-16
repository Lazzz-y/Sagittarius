package io.github.lazzz.sagittarius.system.api.fallback;


import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import io.github.lazzz.sagittarius.system.api.DictFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 系统字典服务远程调用异常后的降级处理类
 *
 * @author Lazzz
 * @date 2025/10/15 21:16
**/
@Slf4j
@Component
public class DictFeignFallbackClient implements DictFeignClient {

    @Override
    public List<DictDetailDTO> getDictDetailDTO(String typeCode) {
        log.error("字典服务熔断降级");
        return Collections.emptyList();
    }
}

