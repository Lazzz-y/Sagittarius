package io.github.lazzz.sagittarius.common.redisson.service.impl;


import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import io.github.lazzz.sagittarius.common.redisson.service.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.io.Closeable;

/**
 * 读写锁服务实现
 *
 * @author Lazzz
 * @date 2025/10/17 20:46
 **/
@Slf4j
@RequiredArgsConstructor
public class ReadWriteLockServiceImpl implements LockService, Closeable {

    private final RedissonClient redissonClient;
    private LockInfo lockInfo;

    // 记录当前持有的具体锁对象（读锁或写锁）
    private RLock currentLock;


    @Override
    public void setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;

        // 默认获取读锁
        if (lockInfo.getLockType() == null) {
            lockInfo.setLockType(LockInfo.LockType.READ);
        }
    }

    @Override
    public boolean lock() {
        if (lockInfo == null) {
            log.error("锁信息不能为空，请先调用setLockInfo设置锁信息");
            return false;
        }
        try {
            RReadWriteLock rwLock = redissonClient.getReadWriteLock(lockInfo.getName());
            // 根据锁类型获取对应的锁
            currentLock = lockInfo.getLockType() == LockInfo.LockType.READ ? rwLock.readLock() : rwLock.writeLock();
            // 默认获取读锁，如需写锁可改为rwLock.writeLock()
            return currentLock.tryLock(
                    lockInfo.getWaitTime(),
                    lockInfo.getLeaseTime(),
                    lockInfo.getTimeUnit()
            );
        } catch (InterruptedException e) {
            log.info("获取{}锁时线程被中断, 锁名称: {}", lockInfo.getLockType(), lockInfo.getName());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("获取{}锁发生异常, 锁名称: {}", lockInfo.getLockType(), lockInfo.getName(), e);
        }
        return false;
    }

    @Override
    public void releaseLock() {
        if (currentLock == null) {
            log.warn("释放锁失败，锁对象为空");
            return;
        }
        try {
            // 释放对应的读锁
            if (currentLock.isHeldByCurrentThread()) {
                currentLock.unlockAsync();
                log.debug("成功释放{}锁，锁名称: {}", lockInfo.getLockType(), lockInfo.getName());
            } else {
                log.warn("当前线程未持有该{}锁，无法释放，锁名称: {}", lockInfo.getLockType(), lockInfo.getName());
            }
        } catch (Exception e) {
            log.error("释放{}锁发生异常, 锁名称: {}", lockInfo.getLockType(), lockInfo.getName(), e);
        } finally {
            currentLock = null;
            lockInfo = null;
        }
    }

    @Override
    public void close() {
        this.releaseLock();
    }
}

