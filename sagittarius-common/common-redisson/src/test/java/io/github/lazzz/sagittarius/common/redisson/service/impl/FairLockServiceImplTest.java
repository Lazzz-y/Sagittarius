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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FairLockServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(FairLockServiceImplTest.class);
    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @InjectMocks
    private FairLockServiceImpl fairLockService;

    private LockInfo lockInfo;

    @BeforeEach
    void setUp() {
        lockInfo = LockInfo.builder()
                .name("test-lock")
                .waitTime(10)
                .leaseTime(30)
                .timeUnit(TimeUnit.SECONDS)
                .build();
    }

    // 原有测试保持不变...
    @Test
    void testLock_Success() throws InterruptedException {
        when(redissonClient.getFairLock(eq("test-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        fairLockService.setLockInfo(lockInfo);
        boolean result = fairLockService.lock();

        assertTrue(result);
        verify(redissonClient).getFairLock("test-lock");
        verify(rLock).tryLock(10, 30, TimeUnit.SECONDS);
    }

    @Test
    void testLock_WithoutLockInfo() {
        boolean result = fairLockService.lock();
        assertFalse(result);
    }

    @Test
    void testLock_InterruptedException() throws InterruptedException {
        when(redissonClient.getFairLock(eq("test-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenThrow(InterruptedException.class);

        fairLockService.setLockInfo(lockInfo);
        boolean result = fairLockService.lock();

        assertFalse(result);
    }

    @Test
    void testReleaseLock_Success() {
        when(redissonClient.getFairLock(eq("test-lock"))).thenReturn(rLock);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        fairLockService.setLockInfo(lockInfo);
        fairLockService.lock();
        fairLockService.releaseLock();

        verify(rLock).unlockAsync();
    }

    @Test
    void testReleaseLock_NotHeldByCurrentThread() {
        when(redissonClient.getFairLock(eq("test-lock"))).thenReturn(rLock);
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        fairLockService.setLockInfo(lockInfo);
        fairLockService.lock();
        fairLockService.releaseLock();

        verify(rLock, never()).unlockAsync();
    }

    @Test
    void testReleaseLock_NullLock() {
        assertDoesNotThrow(() -> fairLockService.releaseLock());
    }

    // 新增测试：验证 try-with-resources 自动释放锁
    @Test
    void testTryWithResources_AutoReleaseLock() throws Exception {
        // 准备
        when(redissonClient.getFairLock(eq("test-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        // 执行：使用 try-with-resources
        try (FairLockServiceImpl service = new FairLockServiceImpl(redissonClient)) {
            service.setLockInfo(lockInfo);
            service.lock();
            // 验证锁已获取
            verify(rLock).tryLock(10, 30, TimeUnit.SECONDS);
        }
        // 离开作用域后自动调用 close() -> releaseLock()

        // 验证：确认解锁方法被调用
        verify(rLock).unlockAsync();
    }

    @Test
    void testTryWithResources_AutoReleaseOnException() throws Exception {
        // 准备
        when(redissonClient.getFairLock(eq("test-lock"))).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        // 执行：在 try 块内抛出异常
        assertThrows(IllegalStateException.class, () -> {
            try (FairLockServiceImpl service = new FairLockServiceImpl(redissonClient)) {
                service.setLockInfo(lockInfo);
                service.lock();
                throw new IllegalStateException("测试异常");
            }
        });

        // 验证：即使发生异常，仍会自动解锁
        verify(rLock).unlockAsync();
    }
}