package io.github.lazzz.sagittarius.jetcache.service;


import com.alicp.jetcache.Cache;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import io.github.lazzz.sagittarius.common.redisson.model.LockType;
import io.github.lazzz.sagittarius.common.redisson.service.LockService;
import io.github.lazzz.sagittarius.system.api.DictFeignClient;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
public class DictCacheService {

    private final DictFeignClient dictFeignClient;

    private final Cache<String, List<DictDetailDTO>> dictCache;

    // 读写锁服务（需保证实现了ReadWriteLock语义）
    private final ObjectProvider<LockService> lockServiceProvider;

    public DictCacheService(
            DictFeignClient dictFeignClient,
            Cache<String, List<DictDetailDTO>> dictCache,
            @Qualifier("readWriteLockService")
            ObjectProvider<LockService> lockServiceProvider) {
        this.dictFeignClient = dictFeignClient;
        this.dictCache = dictCache;
        this.lockServiceProvider = lockServiceProvider;
    }

    /**
     * 根据字典类型编码获取字典项列表
     * 优先从缓存获取，缓存未命中则从远程服务获取并更新缓存
     *
     * @param typeCode 字典类型编码
     * @return {@link List<DictDetailDTO>} 字典项列表
     */
//    public List<DictDetailDTO> getDictByType(String typeCode) {
//        String lockKey = CacheConstants.DICT_LOCK_PREFIX + typeCode;
//        String cacheKey = CacheConstants.DICT_PREFIX + typeCode;
//        List<DictDetailDTO> rs = dictCache.get(cacheKey);
//        // 命中缓存 直接返回
//        if (rs != null) {
//            log.debug("Dict: 缓存命中 | typeCode: {}", typeCode);
//            return rs;
//        }
//        // 缓存未命中 需要写入缓存，所以先创建写锁
//        var lockInfo = getLockInfo(lockKey, 5L, 30L, LockInfo.LockType.WRITE);
//        try(LockService lock = lockService) {
//            lock.setLockInfo(lockInfo);
//            if (lock.lock()){
//                // 锁内二次查询缓存，防止多线程等待锁时已有线程更新缓存
//                rs = dictCache.get(cacheKey);
//                if (rs != null) {
//                    log.debug("Dict: 缓存已命中，已获取锁，二次查询缓存成功 | typeCode: {}", typeCode);
//                    return rs;
//                }
//                // 缓存未命中，从远程服务获取并更新缓存
//                log.debug("Dict: 缓存未命中，从远程服务获取并更新缓存，typeCode: {}", typeCode);
//                rs = dictFeignClient.getDictDetailDTO(typeCode);
//                // 更新缓存
//                dictCache.put(cacheKey, rs);
//                return rs;
//            } else {
//                log.debug("获取字典写锁失败，typeCode: {}", typeCode);
//            }
//        } catch (IOException e) {
//            log.error("获取字典项异常，typeCode: {}", typeCode, e);
//            throw new IllegalStateException("获取字典项失败");
//        }
//        return rs;
//    }

    public List<DictDetailDTO> getDictByType(String typeCode) {
        String cacheKey = CacheConstants.DICT_PREFIX + typeCode;
        List<DictDetailDTO> dictList = dictCache.get(cacheKey);
        if (dictList != null) {
            log.debug("Dict: 缓存命中 | {}", dictList);
            return dictList;
        }

        // 尝试获取读锁，二次检查缓存
        LockInfo readLockInfo = getReadLockInfo(typeCode);
        // 读锁独立实例，自动释放
        try (LockService readLock = lockServiceProvider.getObject()) {
            readLock.setLockInfo(readLockInfo);
            if (readLock.lock()) {
                dictList = dictCache.get(cacheKey);
                if (dictList != null) {
                    log.debug("Dict: 读锁内缓存命中 | {}", dictList);
                    return dictList;
                }
                // 读锁内未命中，无需手动释放，退出try块后自动释放读锁
            } else {
                log.error("获取读锁失败 | typeCode: {}", typeCode);
                throw new IllegalStateException("获取读锁失败");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 读锁释放后，获取写锁加载数据
        LockInfo writeLockInfo = getWriteLockInfo(typeCode);
        // 写锁独立实例，自动释放
        try (LockService writeLock = lockServiceProvider.getObject()) {
            writeLock.setLockInfo(writeLockInfo);
            if (writeLock.lock()) {
                // 防止其他线程已更新缓存
                dictList = dictCache.get(cacheKey);
                if (dictList != null) {
                    log.debug("Dict: 写锁内缓存命中 | {}", dictList);
                    return dictList;
                }
                // 加载数据并更新缓存
                dictList = dictFeignClient.getDictDetailDTO(typeCode);
                dictCache.put(cacheKey, dictList);
                log.debug("Dict: 写锁内缓存更新成功 | {}", dictList);
                return dictList;
            } else {
                log.error("获取写锁失败 | typeCode: {}", typeCode);
                throw new IllegalStateException("获取写锁失败");
            }
        } catch (IOException e) {
            log.error("字典操作异常 | typeCode: {}", typeCode, e);
            throw new IllegalStateException("获取字典项失败");
        }
    }

    public void evictDictCache(String typeCode) {
        String cacheKey = CacheConstants.DICT_PREFIX + typeCode;

        // 删除缓存属于写操作，需获取写锁，防止并发删除/读取导致的不一致
        LockInfo writeLockInfo = getWriteLockInfo(typeCode);
        try (LockService writeLock = lockServiceProvider.getObject()) {
            writeLock.setLockInfo(writeLockInfo);
            if (writeLock.lock()) {
                dictCache.remove(cacheKey);
                log.debug("Dict: 缓存删除成功 | typeCode: {}", typeCode);
            } else {
                log.error("删除缓存时获取写锁失败 | typeCode: {}", typeCode);
                throw new IllegalStateException("删除缓存失败，获取写锁超时");
            }
        } catch (IOException e) {
            log.error("删除字典缓存异常 | typeCode: {}", typeCode, e);
            throw new IllegalStateException("删除字典缓存失败");
        }
    }

    private LockInfo getReadLockInfo(String typeCode) {
        return LockInfo.builder()
                .name(CacheConstants.DICT_LOCK_PREFIX + typeCode)
                .waitTime(5L)
                .leaseTime(30L)
                .timeUnit(TimeUnit.SECONDS)
                .lockType(LockType.READ)
                .build();
    }

    private LockInfo getWriteLockInfo(String typeCode) {
        return LockInfo.builder()
                .name(CacheConstants.DICT_LOCK_PREFIX + typeCode)
                .waitTime(5L)
                .leaseTime(30L)
                .timeUnit(TimeUnit.SECONDS)
                .lockType(LockType.WRITE)
                .build();
    }

}

