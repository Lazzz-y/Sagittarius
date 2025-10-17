package io.github.lazzz.sagittarius.jetcache.service;


import com.alicp.jetcache.Cache;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.system.api.DictFeignClient;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 数据字典缓存服务
 *
 * @author Lazzz
 * @date 2025/10/15 21:42
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class DictCacheService {

    private final DictFeignClient dictFeignClient;

    private final Cache<String, List<DictDetailDTO>> dictCache;

    private final RedissonClient redissonClient;

    /**
     * 根据字典类型编码获取字典项列表
     * 优先从缓存获取，缓存未命中则从远程服务获取并更新缓存
     *
     * @param typeCode 字典类型编码
     * @return {@link List<DictDetailDTO>} 字典项列表
     */
    public List<DictDetailDTO> getDictByType(String typeCode) {
        String lockKey = CacheConstants.DICT_LOCK_PREFIX + typeCode;
        String cacheKey = CacheConstants.DICT_PREFIX + typeCode;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 命中缓存 直接返回
            List<DictDetailDTO> rs = dictCache.get(cacheKey);
            if (rs != null) {
                System.out.println("缓存命中");
                return rs;
            }
            // 缓存未命中，加锁
            var locked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            System.out.println("抢锁");
            if (locked) {
                try {
                    // 锁内二次查缓存(防止等待期间其他节点已加载)
                    rs = dictCache.get(cacheKey);
                    if (rs != null) {
                        System.out.println("锁内二次查找");
                        return rs;
                    }
                    // 查数据(仅一个节点执行)
                    rs = dictFeignClient.getDictDetailDTO(typeCode);
                    if (rs != null){
                        System.out.println("数据库查询数据");
                        dictCache.put(cacheKey, rs);
                    }
                    return rs;
                } finally {
                    System.out.println("锁内逻辑结束，释放锁");
                    // 释放锁(锁内逻辑执行完必须释放)
                    lock.unlock();
                }
            } else {
                log.error("获取锁失败");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取锁被中断", e);
        }
        throw new RuntimeException("获取锁异常");
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

