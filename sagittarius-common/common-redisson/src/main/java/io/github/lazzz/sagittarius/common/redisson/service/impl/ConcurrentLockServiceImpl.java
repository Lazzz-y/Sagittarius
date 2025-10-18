package io.github.lazzz.sagittarius.common.redisson.service.impl;

import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import io.github.lazzz.sagittarius.common.redisson.service.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.io.Closeable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 联锁服务实现
 * 联锁需要同时获取所有指定的锁才认为加锁成功
 *
 * @author Lazzz
 * @date 2025/10/17 22:45
 **/
@Slf4j
@RequiredArgsConstructor
public class ConcurrentLockServiceImpl implements LockService, Closeable {

    private final RedissonClient redissonClient;
    private RLock multiLock;
    private LockInfo lockInfo;

    @Override
    public void setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
    }

    @Override
    public boolean lock() {
        if (lockInfo == null) {
            log.error("锁信息不能为空，请先调用setLockInfo设置锁信息");
            return false;
        }

        List<String> keyList = lockInfo.getKeyList();
        if (keyList == null || keyList.isEmpty()) {
            log.error("联锁需要至少一个锁键，锁名称: {}", lockInfo.getName());
            return false;
        }

        try {
            // 获取多个锁对象
            List<RLock> locks = keyList.stream()
                    .map(redissonClient::getLock)
                    .toList();

            // 创建联锁
            multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[0]));
            
            // 尝试获取锁
            return multiLock.tryLock(
                    lockInfo.getWaitTime(),
                    lockInfo.getLeaseTime(),
                    lockInfo.getTimeUnit()
            );
        } catch (InterruptedException e) {
            log.info("获取联锁时线程被中断, 锁名称: {}", lockInfo.getName());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("获取联锁发生异常, 锁名称: {}", lockInfo.getName(), e);
        }
        return false;
    }

    @Override
    public void releaseLock() {
        if (multiLock == null) {
            log.warn("释放联锁失败，锁对象为空");
            return;
        }

        if (multiLock.isHeldByCurrentThread()) {
            try {
                multiLock.unlockAsync();
                log.debug("成功释放联锁，锁名称: {}", lockInfo.getName());
            } catch (Exception e) {
                log.error("释放联锁发生异常", e);
            }
        } else {
            log.warn("当前线程未持有该联锁，无法释放，锁名称: {}", lockInfo.getName());
        }
    }

    @Override
    public void close() {
        releaseLock();
    }
}