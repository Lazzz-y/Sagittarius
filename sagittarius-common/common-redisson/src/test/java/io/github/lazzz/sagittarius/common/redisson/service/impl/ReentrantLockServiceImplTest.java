package io.github.lazzz.sagittarius.common.redisson.service.impl;

import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReentrantLockServiceImplTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @InjectMocks
    private ReentrantLockServiceImpl reentrantLockService;

    private LockInfo lockInfo;

    @BeforeEach
    void setUp() {
        lockInfo = LockInfo.builder()
                .name("test-reentrant-lock")
                .waitTime(10)
                .leaseTime(30)
                .timeUnit(TimeUnit.SECONDS)
                .build();
    }

    @Test
    void testLock_Success() throws InterruptedException {
        when(redissonClient.getLock(eq("test-reentrant-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        reentrantLockService.setLockInfo(lockInfo);
        boolean result = reentrantLockService.lock();

        assertTrue(result);
        verify(redissonClient).getLock("test-reentrant-lock");
        verify(rLock).tryLock(10, 30, TimeUnit.SECONDS);
    }

    @Test
    void testLock_WithoutLockInfo() {
        boolean result = reentrantLockService.lock();
        assertFalse(result);
    }

    @Test
    void testLock_InterruptedException() throws InterruptedException {
        when(redissonClient.getLock(eq("test-reentrant-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenThrow(InterruptedException.class);

        reentrantLockService.setLockInfo(lockInfo);
        boolean result = reentrantLockService.lock();

        assertFalse(result);
    }

    @Test
    void testReleaseLock_Success() throws InterruptedException {
        when(redissonClient.getLock(eq("test-reentrant-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        reentrantLockService.setLockInfo(lockInfo);
        reentrantLockService.lock();
        reentrantLockService.releaseLock();

        verify(rLock).unlockAsync();
    }

    @Test
    void testReleaseLock_NotHeldByCurrentThread() throws InterruptedException {
        when(redissonClient.getLock(eq("test-reentrant-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        reentrantLockService.setLockInfo(lockInfo);
        reentrantLockService.lock();
        reentrantLockService.releaseLock();

        verify(rLock, never()).unlockAsync();
    }

    @Test
    void testReleaseLock_NullLock() {
        assertDoesNotThrow(() -> reentrantLockService.releaseLock());
    }

    @Test
    void testTryWithResources_AutoReleaseLock() throws Exception {
        when(redissonClient.getLock(eq("test-reentrant-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        try (ReentrantLockServiceImpl service = new ReentrantLockServiceImpl(redissonClient)) {
            service.setLockInfo(lockInfo);
            service.lock();
        }

        verify(rLock).unlockAsync();
    }

    @Test
    void testTryWithResources_AutoReleaseOnException() throws InterruptedException {
        when(redissonClient.getLock(eq("test-reentrant-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            try (ReentrantLockServiceImpl service = new ReentrantLockServiceImpl(redissonClient)) {
                service.setLockInfo(lockInfo);
                service.lock();
                throw new IllegalStateException("测试异常");
            }
        });

        verify(rLock).unlockAsync();
    }
}