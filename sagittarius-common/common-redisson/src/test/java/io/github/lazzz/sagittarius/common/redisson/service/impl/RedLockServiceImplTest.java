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
class RedLockServiceImplTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock1;

    @Mock
    private RLock rLock2;

    @Mock
    private RLock redLock;

    @InjectMocks
    private RedLockServiceImpl redLockService;

    private LockInfo lockInfo;
    private List<String> keyList = Arrays.asList("lock1", "lock2", "lock3");

    @BeforeEach
    void setUp() {
        lockInfo = LockInfo.builder()
                .name("test-red-lock")
                .keyList(keyList)
                .waitTime(10)
                .leaseTime(30)
                .timeUnit(TimeUnit.SECONDS)
                .build();
    }

    @Test
    void testLock_Success() throws InterruptedException {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1, rLock2);
        when(redissonClient.getRedLock(any(RLock[].class))).thenReturn(redLock);
        when(redLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        redLockService.setLockInfo(lockInfo);
        boolean result = redLockService.lock();

        assertTrue(result);
        verify(redissonClient, times(3)).getLock(anyString());
        verify(redissonClient).getRedLock(any(RLock[].class));
        verify(redLock).tryLock(10, 30, TimeUnit.SECONDS);
    }

    @Test
    void testLock_WithoutLockInfo() {
        boolean result = redLockService.lock();
        assertFalse(result);
    }

    @Test
    void testLock_WithoutKeyList() {
        lockInfo.setKeyList(null);
        redLockService.setLockInfo(lockInfo);
        boolean result = redLockService.lock();
        assertFalse(result);
    }

    @Test
    void testLock_InterruptedException() throws InterruptedException {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getRedLock(any(RLock[].class))).thenReturn(redLock);
        when(redLock.tryLock(anyLong(), anyLong(), any())).thenThrow(InterruptedException.class);

        redLockService.setLockInfo(lockInfo);
        boolean result = redLockService.lock();

        assertFalse(result);
    }

    @Test
    void testReleaseLock_Success() {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getRedLock(any(RLock[].class))).thenReturn(redLock);
        when(redLock.isHeldByCurrentThread()).thenReturn(true);

        redLockService.setLockInfo(lockInfo);
        redLockService.lock();
        redLockService.releaseLock();

        verify(redLock).unlockAsync();
    }

    @Test
    void testReleaseLock_NotHeldByCurrentThread() {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getRedLock(any(RLock[].class))).thenReturn(redLock);
        when(redLock.isHeldByCurrentThread()).thenReturn(false);

        redLockService.setLockInfo(lockInfo);
        redLockService.lock();
        redLockService.releaseLock();

        verify(redLock, never()).unlockAsync();
    }

    @Test
    void testReleaseLock_NullLock() {
        assertDoesNotThrow(() -> redLockService.releaseLock());
    }

    @Test
    void testTryWithResources_AutoReleaseLock() throws Exception {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getRedLock(any(RLock[].class))).thenReturn(redLock);
        when(redLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(redLock.isHeldByCurrentThread()).thenReturn(true);

        try (RedLockServiceImpl service = new RedLockServiceImpl(redissonClient)) {
            service.setLockInfo(lockInfo);
            service.lock();
        }

        verify(redLock).unlockAsync();
    }

    @Test
    void testTryWithResources_AutoReleaseOnException() throws InterruptedException {
        when(redissonClient.getLock(anyString())).thenReturn(rLock1);
        when(redissonClient.getRedLock(any(RLock[].class))).thenReturn(redLock);
        when(redLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(redLock.isHeldByCurrentThread()).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            try (RedLockServiceImpl service = new RedLockServiceImpl(redissonClient)) {
                service.setLockInfo(lockInfo);
                service.lock();
                throw new IllegalStateException("测试异常");
            }
        });

        verify(redLock).unlockAsync();
    }
}