package io.github.lazzz.sagittarius.common.redisson.model;

/**
 * 锁类型
 *
 * @author Lazzz
 * @date 2025/10/19 21:17
 **/
public enum LockType {
        /**
         * 读锁
         */
        READ,
        /**
         * 写锁
         */
        WRITE,
        /**
         * 公平锁
         */
        FAIR,
        /**
         * 红锁
         */
        RED,
        /**
         * 可重入锁
         */
        REENTRANT,
        /**
         * 联锁
         */
        CONCURRENT;
    }