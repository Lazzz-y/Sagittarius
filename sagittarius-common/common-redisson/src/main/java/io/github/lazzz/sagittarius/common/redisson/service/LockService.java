package io.github.lazzz.sagittarius.common.redisson.service;

import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;

import java.io.Closeable;

/**
 * 锁服务
 *
 * @author Lazzz
 * @date 2025/10/17 18:59
 **/
public interface LockService extends Closeable {

    /**
     * 添加锁信息
     *
     * @param lockInfo 锁信息
     */
    void setLockInfo(LockInfo lockInfo);

    /**
     * 加锁
     *
     * @return boolean
     */
    boolean lock();

    /**
     * 释放锁
     *
     */
    void releaseLock();
}
