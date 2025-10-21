package io.github.lazzz.sagittarius.common.redisson.model;

import lombok.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 锁信息
 *
 * @author Lazzz
 * @date 2025/10/17 18:59
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockInfo {

    /**
     * 锁的名称
     */
    private String name;

    /**
     * 获取锁的线程唯一标识（可重入锁使用）
     */
    private String threadUniqueIdentifier;

    /**
     * 等待时间
     */
    private long waitTime;

    /**
     * 锁过期自动释放时间
     */
    private long leaseTime;

    /**
     * 锁等待的时间单位（默认秒）
     */
    @Builder.Default
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 锁类型
     */
    private LockType lockType;

    /**
     * key集合，用于红锁和联锁
     */
    private List<String> keyList;


    public LockInfo(String name, long waitTime, long leaseTime, TimeUnit timeUnit) {
        this.name = name;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
        this.timeUnit = timeUnit;
    }

    public LockInfo(String name, List<String> keyList, long waitTime, long leaseTime, TimeUnit timeUnit) {
        this.name = name;
        this.keyList = keyList;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
        this.timeUnit = timeUnit;
    }

}
