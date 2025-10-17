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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcurrentLockServiceImplTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock1;

    @Mock
    private RLock rLock2;

    @Mock
    private RLock multiLock;

    @InjectMocks
    private ConcurrentLockServiceImpl concurrentLockService;

    private LockInfo lockInfo;
    private List<String> keyList = Arrays.asList("lock1", "lock2");

    @BeforeEach
    void setUp() {
        lockInfo = LockInfo.builder()
                .name("test-concurrent-lock")
                .keyList(keyList)
                .waitTime(10)
                .leaseTime(30)
                .timeUnit(TimeUnit.SECONDS)
                .build();
    }

    @Test
    void testLock_Success() throws InterruptedException {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1, rLock2);
        when(redissonClient.getMultiLock(any(RLock[].class))).thenReturn(multiLock);
        when(multiLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        concurrentLockService.setLockInfo(lockInfo);
        boolean result = concurrentLockService.lock();

        assertTrue(result);
        verify(redissonClient, times(2)).getLock(anyString());
        verify(redissonClient).getMultiLock(any(RLock[].class));
        verify(multiLock).tryLock(10, 30, TimeUnit.SECONDS);
    }

    @Test
    void testLock_WithoutLockInfo() {
        boolean result = concurrentLockService.lock();
        assertFalse(result);
    }

    @Test
    void testLock_WithoutKeyList() {
        lockInfo.setKeyList(null);
        concurrentLockService.setLockInfo(lockInfo);
        boolean result = concurrentLockService.lock();
        assertFalse(result);
    }

    @Test
    void testLock_InterruptedException() throws InterruptedException {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getMultiLock(any(RLock[].class))).thenReturn(multiLock);
        when(multiLock.tryLock(anyLong(), anyLong(), any())).thenThrow(InterruptedException.class);

        concurrentLockService.setLockInfo(lockInfo);
        boolean result = concurrentLockService.lock();

        assertFalse(result);
    }

    @Test
    void testReleaseLock_Success() {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getMultiLock(any(RLock[].class))).thenReturn(multiLock);
        when(multiLock.isHeldByCurrentThread()).thenReturn(true);

        concurrentLockService.setLockInfo(lockInfo);
        concurrentLockService.lock();
        concurrentLockService.releaseLock();

        verify(multiLock).unlockAsync();
    }

    @Test
    void testReleaseLock_NotHeldByCurrentThread() {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getMultiLock(any(RLock[].class))).thenReturn(multiLock);
        when(multiLock.isHeldByCurrentThread()).thenReturn(false);

        concurrentLockService.setLockInfo(lockInfo);
        concurrentLockService.lock();
        concurrentLockService.releaseLock();

        verify(multiLock, never()).unlockAsync();
    }

    @Test
    void testReleaseLock_NullLock() {
        assertDoesNotThrow(() -> concurrentLockService.releaseLock());
    }

    @Test
    void testTryWithResources_AutoReleaseLock() throws Exception {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getMultiLock(any(RLock[].class))).thenReturn(multiLock);
        when(multiLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(multiLock.isHeldByCurrentThread()).thenReturn(true);

        try (ConcurrentLockServiceImpl service = new ConcurrentLockServiceImpl(redissonClient)) {
            service.setLockInfo(lockInfo);
            service.lock();
        }

        verify(multiLock).unlockAsync();
    }

    @Test
    void testTryWithResources_AutoReleaseOnException() throws InterruptedException {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getMultiLock(any(RLock[].class))).thenReturn(multiLock);
        when(multiLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(multiLock.isHeldByCurrentThread()).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            try (ConcurrentLockServiceImpl service = new ConcurrentLockServiceImpl(redissonClient)) {
                service.setLockInfo(lockInfo);
                service.lock();
                throw new IllegalStateException("测试异常");
            }
        });

        verify(multiLock).unlockAsync();
    }
}