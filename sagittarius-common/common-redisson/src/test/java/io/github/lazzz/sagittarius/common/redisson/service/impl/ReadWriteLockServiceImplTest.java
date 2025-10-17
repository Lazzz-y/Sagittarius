package io.github.lazzz.sagittarius.common.redisson.service.impl;

import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadWriteLockServiceImplTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RReadWriteLock rReadWriteLock;

    @Mock
    private RLock readLock;

    @Mock
    private RLock writeLock;

    @InjectMocks
    private ReadWriteLockServiceImpl readWriteLockService;

    private LockInfo readLockInfo;
    private LockInfo writeLockInfo;

    @BeforeEach
    void setUp() {
        readLockInfo = LockInfo.builder()
                .name("test-rw-lock")
                .lockType(LockInfo.LockType.READ)
                .waitTime(10)
                .leaseTime(30)
                .timeUnit(TimeUnit.SECONDS)
                .build();

        writeLockInfo = LockInfo.builder()
                .name("test-rw-lock")
                .lockType(LockInfo.LockType.WRITE)
                .waitTime(10)
                .leaseTime(30)
                .timeUnit(TimeUnit.SECONDS)
                .build();
    }

    /**
     * 测试读锁成功获取锁
     */
    @Test
    void testReadLock_Success() throws InterruptedException {
        when(redissonClient.getReadWriteLock(anyString())).thenReturn(rReadWriteLock);
        when(rReadWriteLock.readLock()).thenReturn(readLock);
        when(readLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        readWriteLockService.setLockInfo(readLockInfo);
        boolean result = readWriteLockService.lock();

        assertTrue(result);
        verify(rReadWriteLock).readLock();
        verify(readLock).tryLock(10, 30, TimeUnit.SECONDS);
    }

    /**
     * 测试写锁成功获取锁
     */
    @Test
    void testWriteLock_Success() throws InterruptedException {
        when(redissonClient.getReadWriteLock(anyString())).thenReturn(rReadWriteLock);
        when(rReadWriteLock.writeLock()).thenReturn(writeLock);
        when(writeLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        readWriteLockService.setLockInfo(writeLockInfo);
        boolean result = readWriteLockService.lock();

        assertTrue(result);
        verify(rReadWriteLock).writeLock();
        verify(writeLock).tryLock(10, 30, TimeUnit.SECONDS);
    }

    /**
     * 测试默认为读锁成功获取锁
     */
    @Test
    void testLock_DefaultToReadLock() throws InterruptedException {
        LockInfo defaultLockInfo = LockInfo.builder()
                .name("test-default-lock")
                .waitTime(10)
                .leaseTime(30)
                .build();

        when(redissonClient.getReadWriteLock(anyString())).thenReturn(rReadWriteLock);
        when(rReadWriteLock.readLock()).thenReturn(readLock);
        when(readLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        readWriteLockService.setLockInfo(defaultLockInfo);
        boolean result = readWriteLockService.lock();

        assertTrue(result);
        verify(rReadWriteLock).readLock();
    }

    /**
     * 测试没有锁信息时获取锁失败
     */
    @Test
    void testLock_WithoutLockInfo() {
        boolean result = readWriteLockService.lock();
        assertFalse(result);
    }

    /**
     * 测试获取锁时抛出中断异常
     */
    @Test
    void testLock_InterruptedException() throws InterruptedException {
        when(redissonClient.getReadWriteLock(anyString())).thenReturn(rReadWriteLock);
        when(rReadWriteLock.readLock()).thenReturn(readLock);
        when(readLock.tryLock(anyLong(), anyLong(), any())).thenThrow(InterruptedException.class);

        readWriteLockService.setLockInfo(readLockInfo);
        boolean result = readWriteLockService.lock();

        assertFalse(result);
    }

    /**
     * 测试释放读锁成功
     */
    @Test
    void testReleaseReadLock_Success() throws InterruptedException {
        when(redissonClient.getReadWriteLock(anyString())).thenReturn(rReadWriteLock);
        when(rReadWriteLock.readLock()).thenReturn(readLock);
        when(readLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(readLock.isHeldByCurrentThread()).thenReturn(true);

        readWriteLockService.setLockInfo(readLockInfo);
        readWriteLockService.lock();
        readWriteLockService.releaseLock();

        verify(readLock).unlockAsync();
    }

    /**
     * 测试释放写锁成功
     */
    @Test
    void testReleaseWriteLock_Success() throws InterruptedException {
        when(redissonClient.getReadWriteLock(anyString())).thenReturn(rReadWriteLock);
        when(rReadWriteLock.writeLock()).thenReturn(writeLock);
        when(writeLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(writeLock.isHeldByCurrentThread()).thenReturn(true);

        readWriteLockService.setLockInfo(writeLockInfo);
        readWriteLockService.lock();
        readWriteLockService.releaseLock();

        verify(writeLock).unlockAsync();
    }


    /**
     * 测试释放锁时锁不属于当前线程
     */
    @Test
    void testReleaseLock_NotHeldByCurrentThread() throws InterruptedException {
        when(redissonClient.getReadWriteLock(anyString())).thenReturn(rReadWriteLock);
        when(rReadWriteLock.readLock()).thenReturn(readLock);
        when(readLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(readLock.isHeldByCurrentThread()).thenReturn(false);

        readWriteLockService.setLockInfo(readLockInfo);
        readWriteLockService.lock();
        readWriteLockService.releaseLock();

        verify(readLock, never()).unlockAsync();
    }

    /**
     * 测试释放锁时锁为null
     */
    @Test
    void testReleaseLock_NullLock() {
        assertDoesNotThrow(() -> readWriteLockService.releaseLock());
    }

    /**
     * 测试异常情况下的自动释放锁
     */
    @Test
    void testTryWithResources_AutoReleaseLock() throws Exception {
        when(redissonClient.getReadWriteLock(anyString())).thenReturn(rReadWriteLock);
        when(rReadWriteLock.writeLock()).thenReturn(writeLock);
        when(writeLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(writeLock.isHeldByCurrentThread()).thenReturn(true);

        try (ReadWriteLockServiceImpl service = new ReadWriteLockServiceImpl(redissonClient)) {
            service.setLockInfo(writeLockInfo);
            service.lock();
        }

        verify(writeLock).unlockAsync();
    }
}