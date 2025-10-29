package io.github.lazzz.sagittarius.system;

import io.github.lazzz.sagittarius.dict.service.DictCacheService;
import io.github.lazzz.sagittarius.system.api.DictFeignClient;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DictCacheServiceConcurrentTest {

    @Autowired
    private DictCacheService dictCacheService;

    @MockBean
    private DictFeignClient dictFeignClient;

    private final String testTypeCode = "sex";

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        List<DictDetailDTO> mockData = new ArrayList<>();
        DictDetailDTO dto = new DictDetailDTO();
        dto.setTypeCode(testTypeCode);dto.setName("未知");dto.setValue("0");
        mockData.add(dto);
        dto = new DictDetailDTO();
        dto.setTypeCode(testTypeCode);dto.setName("男");dto.setValue("1");
        mockData.add(dto);
        dto = new DictDetailDTO();
        dto.setTypeCode(testTypeCode);dto.setName("女");dto.setValue("2");
        mockData.add(dto);
        
        // 模拟远程调用返回数据
        Mockito.when(dictFeignClient.getDictDetailDTO(eq(testTypeCode))).thenReturn(mockData);
        dictCacheService.evictDictCache(testTypeCode);
    }

    @Test
    void testConcurrentGetDictByType() throws InterruptedException {
        int threadCount = 5; // 模拟20个并发线程
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1); // 用于统一启动所有线程
        CountDownLatch endLatch = new CountDownLatch(threadCount); // 等待所有线程完成
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始信号
                    List<DictDetailDTO> result = dictCacheService.getDictByType(testTypeCode);
                    assertNotNull(result, "返回结果为空");
                    assertEquals(3, result.size(), "返回结果数量不正确");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 启动所有线程
        startLatch.countDown();
        // 等待所有线程执行完成
        endLatch.await();
        executor.shutdown();

        // 验证远程服务只被调用一次（分布式锁生效）
        verify(dictFeignClient, times(1)).getDictDetailDTO(eq(testTypeCode));
    }

}