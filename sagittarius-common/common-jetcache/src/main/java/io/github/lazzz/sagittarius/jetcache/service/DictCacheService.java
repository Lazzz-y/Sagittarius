package io.github.lazzz.sagittarius.jetcache.service;


import com.alicp.jetcache.Cache;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import io.github.lazzz.sagittarius.system.api.DictFeignClient;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据字典缓存服务
 *
 * @author Lazzz
 * @date 2025/10/15 21:42
**/
@Service
@RequiredArgsConstructor
public class DictCacheService {

    private final DictFeignClient dictFeignClient;

    private final Cache<String, List<DictDetailDTO>> dictCache;

    /**
     * 根据字典类型编码获取字典项列表
     * 优先从缓存获取，缓存未命中则从远程服务获取并更新缓存
     *
     * @param typeCode 字典类型编码
     * @return {@link List<DictDetailDTO>} 字典项列表
     */
    public List<DictDetailDTO> getDictByType(String typeCode) {
        return dictCache.computeIfAbsent(CacheConstants.DICT_PREFIX + typeCode,
                k ->
                        dictFeignClient.getDictDetailDTO(typeCode)
        );
    }

    /**
     * 刷新指定类型的字典缓存
     *
     * @param typeCode 字典类型编码
     */
    public void refreshDictCache(String typeCode) {
        List<DictDetailDTO> dictList = dictFeignClient.getDictDetailDTO(typeCode);
        dictCache.put(CacheConstants.DICT_PREFIX + typeCode, dictList);
    }

    /**
     * 清除指定类型的字典缓存
     *
     * @param typeCode 字典类型编码
     */
    public void evictDictCache(String typeCode) {
        dictCache.remove(CacheConstants.DICT_PREFIX + typeCode);
    }

    /**
     * 清除所有字典缓存
     */
    public void clearAllDictCache() {
        dictCache.close();
    }

}

