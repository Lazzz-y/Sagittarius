package io.github.lazzz.sagittarius.jetcache.util;

import org.redisson.api.RLock;
import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * 将 Redisson 的 RLock 包装为 AutoCloseable，支持 try-with-resources 自动释放
 */
public class AutoCloseableLock implements Closeable {
    private final RLock lock;

    public AutoCloseableLock(RLock lock) {
        this.lock = lock;
    }

    /**
     * 尝试获取锁（参数与 RLock.tryLock 一致）
     */
    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(waitTime, leaseTime, unit);
    }

    /**
     * 判断当前线程是否持有锁
     */
    public boolean isHeldByCurrentThread() {
        return lock.isHeldByCurrentThread();
    }

    /**
     * 自动释放锁（try-with-resources 会自动调用）
     */
    @Override
    public void close() {
        // 仅释放当前线程持有的锁，避免释放其他线程的锁
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}