package io.github.lazzz.sagittarius.common.redisson.model;


import io.github.lazzz.sagittarius.common.redisson.annotation.Lock;
import org.redisson.api.RLock;

/**
 * 失败策略枚举
 *
 * @author Lazzz
 * @date 2025/10/19 21:26
 **/
public enum LockStrategy {

    // 跳过快速失败
    SKIP_FAST(){
        @Override
        public boolean tryLock(RLock lock, Lock prop) throws InterruptedException {
            return lock.tryLock(0, prop.leaseTime(), prop.timeUnit());
        }
    },
    // 快速失败
    FAIL_FAST(){
        @Override
        public boolean tryLock(RLock lock, Lock prop) throws InterruptedException {
            boolean isLock = lock.tryLock(0, prop.leaseTime(), prop.timeUnit());
            if (!isLock) {
                throw new RuntimeException("请求太频繁");
            }
            return true;
        }
    },
    // 循环尝试
    KEEP_TRYING(){
        @Override
        public boolean tryLock(RLock lock, Lock prop) throws InterruptedException {
            lock.lock( prop.leaseTime(), prop.timeUnit());
            return true;
        }
    },
    // 循环尝试，超时失败
    SKIP_AFTER_RETRY_TIMEOUT(){
        @Override
        public boolean tryLock(RLock lock, Lock prop) throws InterruptedException {
            return lock.tryLock(prop.waitTime(), prop.leaseTime(), prop.timeUnit());
        }
    },
    // 循环尝试，超时失败
    FAIL_AFTER_RETRY_TIMEOUT(){
        @Override
        public boolean tryLock(RLock lock, Lock prop) throws InterruptedException {
            boolean isLock = lock.tryLock(prop.waitTime(), prop.leaseTime(), prop.timeUnit());
            if (!isLock) {
                throw new RuntimeException("请求太频繁");
            }
            return true;
        }
    },
    ;

    public abstract boolean tryLock(RLock lock, Lock prop) throws InterruptedException;

}

