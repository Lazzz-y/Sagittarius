package io.github.lazzz.sagittarius.common.redisson.lock;



/**
 * 锁接口
 * 
 * @author Lazzz 
 * @date 2025/10/20 13:10
**/
public interface ILock {

    /**
     * 获取锁
     *
     * @return {@link Boolean}
     */
    boolean acquire();

    /**
     * 释放锁
     */
    void release();

}

