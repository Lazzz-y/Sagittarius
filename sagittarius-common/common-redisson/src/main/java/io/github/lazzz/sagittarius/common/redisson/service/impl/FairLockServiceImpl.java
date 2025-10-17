package io.github.lazzz.sagittarius.common.redisson.service.impl;

import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import io.github.lazzz.sagittarius.common.redisson.service.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.io.Closeable;

/**
 * 公平锁服务实现
 *
 * @author Lazzz
 * @date 2025/10/17 19:01
 **/
@Slf4j
@RequiredArgsConstructor
public class FairLockServiceImpl implements LockService, Closeable {

    private final RedissonClient redissonClient;
    private RLock fairLock;
    private LockInfo lockInfo;

    @Override
    public void setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
    }

    @Override
    public boolean lock() {
        // 新增：检查lockInfo是否为空
        if (lockInfo == null) {
            log.error("锁信息不能为空，请先调用setLockInfo设置锁信息");
            return false;
        }

        try {
            fairLock = redissonClient.getFairLock(lockInfo.getName());
            return fairLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), lockInfo.getTimeUnit());
        } catch (InterruptedException e) {
            log.info("获取公平锁时线程被意外中断，锁名称: {}", lockInfo.getName(), e);
            Thread.currentThread().interrupt(); // 恢复中断状态
        } catch (Exception e) {
            log.error("获取公平锁发生异常，锁名称: {}", lockInfo.getName(), e);
        }
        return false;
    }

    @Override
    public void releaseLock() {
        // 新增：检查fairLock是否为空
        if (fairLock == null) {
            log.warn("释放锁失败，锁对象为空");
            return;
        }

        if (fairLock.isHeldByCurrentThread()){
            try {
                fairLock.unlockAsync();
                log.debug("成功释放公平锁，锁名称: {}", lockInfo != null ? lockInfo.getName() : "未知");
            } catch (Exception e) {
                log.error("释放公平锁发生异常", e);
            }
        } else {
            log.warn("当前线程未持有该锁，无法释放，锁名称: {}", lockInfo != null ? lockInfo.getName() : "未知");
        }
    }

    @Override
    public void close() {
        releaseLock();
    }
}