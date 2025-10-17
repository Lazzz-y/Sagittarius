package io.github.lazzz.sagittarius.common.redisson.service.impl;

import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import io.github.lazzz.sagittarius.common.redisson.service.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;

import java.io.Closeable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 红锁服务实现
 * 红锁算法需要多个独立的Redis实例，当超过半数的实例获取锁成功时才认为整体加锁成功
 *
 * @author Lazzz
 * @date 2025/10/17 22:30
 **/
@Slf4j
@RequiredArgsConstructor
public class RedLockServiceImpl implements LockService, Closeable {

    private final RedissonClient redissonClient;
    private RLock redLock;
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
            log.error("红锁需要至少一个锁键，锁名称: {}", lockInfo.getName());
            return false;
        }

        try {
            // 获取多个Redis实例的锁
            List<RLock> locks = keyList.stream()
                    .map(redissonClient::getLock)
                    .collect(Collectors.toList());

            // 创建红锁
            redLock = redissonClient.getRedLock(locks.toArray(new RLock[0]));
            
            // 尝试获取锁
            return redLock.tryLock(
                    lockInfo.getWaitTime(),
                    lockInfo.getLeaseTime(),
                    lockInfo.getTimeUnit()
            );
        } catch (InterruptedException e) {
            log.info("获取红锁时线程被中断, 锁名称: {}", lockInfo.getName());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("获取红锁发生异常, 锁名称: {}", lockInfo.getName(), e);
        }
        return false;
    }

    @Override
    public void releaseLock() {
        if (redLock == null) {
            log.warn("释放红锁失败，锁对象为空");
            return;
        }

        if (redLock.isHeldByCurrentThread()) {
            try {
                redLock.unlockAsync();
                log.debug("成功释放红锁，锁名称: {}", lockInfo.getName());
            } catch (Exception e) {
                log.error("释放红锁发生异常", e);
            }
        } else {
            log.warn("当前线程未持有该红锁，无法释放，锁名称: {}", lockInfo.getName());
        }
    }

    @Override
    public void close() {
        releaseLock();
    }
}