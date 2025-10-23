package io.github.lazzz.sagittarius.system.service;

import io.github.lazzz.sagittarius.system.cache.RouteCacheService;
import io.github.lazzz.sagittarius.system.model.vo.RouteVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class ISysMenuServiceConcurrentTest {

    @Autowired
    private RouteCacheService routeCacheService;

    @Autowired
    private ISysMenuService sysMenuService;

    // 并发线程数（可根据测试需求调整）
    private static final int CONCURRENT_THREADS = 5;

    @Test
    public void test() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(CONCURRENT_THREADS);
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS);
        routeCacheService.refreshMenuCache();
        for (int i = 0; i < CONCURRENT_THREADS; i++){
            executor.submit(() -> {
                try {
                    startLatch.await();
                    long startTime = System.currentTimeMillis();
                    System.out.printf("写线程[%s]开始执行更新操作%n", Thread.currentThread().getName());
                    routeCacheService.refreshMenuCache();
                    long endTime = System.currentTimeMillis();
                    System.out.printf("写线程[%s]执行完成，耗时：%dms%n",
                            Thread.currentThread().getName(),
                            endTime - startTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    endLatch.countDown();
                }

            });
        }
        // 释放所有线程开始信号
        startLatch.countDown();
        // 等待所有线程完成
        endLatch.await(15, TimeUnit.SECONDS);
        executor.shutdown();
        routeCacheService.clearCache();
    }

    /**
     * 测试纯并发读场景：验证多个读线程能否同时获取读锁（无阻塞）
     */
    @Test
    public void testListRoutesConcurrentRead() throws InterruptedException {
        // 用于控制所有线程同时开始
        CountDownLatch startLatch = new CountDownLatch(1);
        // 用于等待所有线程执行完成
        CountDownLatch endLatch = new CountDownLatch(CONCURRENT_THREADS);
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS);

        // 记录成功执行的线程数
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < CONCURRENT_THREADS; i++) {
            executor.submit(() -> {
                try {
                    // 等待开始信号
                    startLatch.await();
                    long startTime = System.currentTimeMillis();

                    // 执行读操作
                    List<RouteVO> routes = sysMenuService.listRoutes();

                    long endTime = System.currentTimeMillis();
                    System.out.printf("线程[%s]执行完成，耗时：%dms，结果大小：%d%n",
                            Thread.currentThread().getName(),
                            endTime - startTime,
                            routes.size());

                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 所有线程就绪后，释放开始信号
        startLatch.countDown();
        // 等待所有线程执行完成（超时时间可根据实际情况调整）
        endLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        // 验证所有线程是否成功执行
        assert successCount.get() == CONCURRENT_THREADS : "部分读线程执行失败";
        System.out.println("纯并发读测试完成，所有线程均成功执行");
        routeCacheService.clearCache();
    }

    /**
     * 测试读写并发场景：验证读操作是否等待写锁释放（确保数据一致性）
     */
    @Test
    public void testListRoutesReadWriteConcurrent() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(CONCURRENT_THREADS + 1); // 1个写线程 + N个读线程
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS + 1);

        // 写线程：模拟菜单更新操作（假设更新会触发写锁）
        executor.submit(() -> {
            try {
                startLatch.await();
                long startTime = System.currentTimeMillis();
                System.out.printf("写线程[%s]开始执行更新操作%n", Thread.currentThread().getName());
                sysMenuService.listRoutes();
                long endTime = System.currentTimeMillis();
                System.out.printf("写线程[%s]执行完成，耗时：%dms%n",
                        Thread.currentThread().getName(),
                        endTime - startTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            } finally {
                endLatch.countDown();
            }
        });

        // 读线程：并发调用listRoutes
        for (int i = 0; i < CONCURRENT_THREADS; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(100);
                    startLatch.await();
                    long startTime = System.currentTimeMillis();
                    System.out.printf("读线程[%s]开始执行读操作%n\n", Thread.currentThread().getName());

                    List<RouteVO> routes = sysMenuService.listRoutes();
                    System.out.printf("读线程[%s]获取结果：%s\n", Thread.currentThread().getName(), routes);
                    long endTime = System.currentTimeMillis();
                    System.out.printf("读线程[%s]执行完成，耗时：%dms，结果大小：%d%n\n",
                            Thread.currentThread().getName(),
                            endTime - startTime,
                            routes.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 释放所有线程开始信号
        startLatch.countDown();
        // 等待所有线程完成
        endLatch.await(15, TimeUnit.SECONDS);
        executor.shutdown();
        routeCacheService.clearCache();
        System.out.println("读写并发测试完成");
    }
}