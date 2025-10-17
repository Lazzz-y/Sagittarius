package io.github.lazzz.sagittarius.common.redisson.service.impl;


import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import io.github.lazzz.sagittarius.common.redisson.service.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 可重入锁服务实现
 * 
 * @author Lazzz 
 * @date 2025/10/17 22:01
**/
@Slf4j
@RequiredArgsConstructor
public class ReentrantLockServiceImpl implements LockService {

    private final RedissonClient redissonClient;
    private RLock reentrantLock;
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
        try {
            reentrantLock = redissonClient.getLock(lockInfo.getName());
            return reentrantLock.tryLock(
                    lockInfo.getWaitTime(),
                    lockInfo.getLeaseTime(),
                    lockInfo.getTimeUnit()
            );
        } catch (InterruptedException e) {
            log.info("获取可重入锁时线程被中断, 锁名称: {}", lockInfo.getName());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("获取可重入锁发生异常, 锁名称: {}", lockInfo.getName(), e);
        }
        return false;
    }

    @Override
    public void releaseLock() {
        if (reentrantLock == null) {
            log.warn("释放可重入锁失败，锁对象为空");
            return;
        }

        if (reentrantLock.isHeldByCurrentThread()) {
            try {
                reentrantLock.unlockAsync();
                log.debug("成功释放可重入锁，锁名称: {}", lockInfo.getName());
            } catch (Exception e) {
                log.error("释放可重入锁异常", e);
            }
        } else {
            log.warn("当前线程未持有可重入锁，无法释放，锁名称: {}", lockInfo.getName());
        }
    }

    @Override
    public void close() {
        this.releaseLock();
    }
}

