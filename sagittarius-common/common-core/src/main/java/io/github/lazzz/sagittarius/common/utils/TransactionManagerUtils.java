package io.github.lazzz.sagittarius.common.utils;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * <a href="https://juejin.cn/post/7374719076696080411"/> 事务管理器工具类
 *
 * @author Lazzz
 * @date 2025/10/25 21:17
 **/
@Slf4j
public class TransactionManagerUtils {

    @Setter
    private static ExecutorService defaultExecutorService = Executors.newCachedThreadPool();

    public static <T> void afterCommitDefault(T args, Consumer<? super T> consumer) {
        afterCommit(args, consumer, defaultExecutorService);
    }

    public static <T> void afterCommit(
            T args,
            Consumer<? super T> consumer,
            ExecutorService executorService
    ) {
        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {
                            @Override
                            public void afterCommit() {
                                executorService.submit(() -> {
                                    long startTime = System.currentTimeMillis();
                                    try {
                                        consumer.accept(args);
                                    } catch (Exception e) {
                                        log.error("Error executing afterCommit", e);
                                    } finally {
                                        long duration = System.currentTimeMillis() - startTime;
                                        log.info("afterCommit executed in {} ms", duration);
                                    }
                                });
                            }
                        }
                );
    }

    public static <T> void afterCommit(
            T args,
            Consumer<? super T> consumer,
            ExecutorService executorService,
            String errorMsg
    ) {
        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {
                            @Override
                            public void afterCommit() {
                                executorService.submit(() -> {
                                    try {
                                        consumer.accept(args);
                                    } catch (Exception e) {
                                        log.error(errorMsg, e);
                                    }
                                });
                            }
                        }
                );
    }

    public static <T> void afterCommit(
            T args,
            Consumer<? super T> consumer
    ) {
        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {
                            @Override
                            public void afterCommit() {
                                consumer.accept(args);
                            }
                        }
                );
    }

    public static <T> void afterCommit(
            Runnable runnable,
            ExecutorService executorService
    ) {
        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {
                            @Override
                            public void afterCommit() {
                                executorService.submit(() -> {
                                    try {
                                        runnable.run();
                                    } catch (Exception e) {
                                        log.error("run fail", e);
                                    }
                                });
                            }
                        }
                );
    }

    /**
     * 增加事务回滚后的操作支持
     *
     * @param args     事务操作的参数
     * @param consumer 事务回滚后的操作
     * @param <T>      参数类型
     */
    public static <T> void afterRollback(T args, Consumer<? super T> consumer) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    consumer.accept(args);
                }
            }
        });
    }

    // 在事务提交后执行给定的操作
    public static void afterCommit(Runnable action) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.run();
            }
        });
    }

    // 在事务回滚后执行给定的操作
    public static void afterRollback(Runnable action) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    action.run();
                }
            }
        });
    }

    // 在事务提交后执行给定的操作，并消费事务结果
    public static <T> void afterCommit(Consumer<T> action, T result) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.accept(result);
            }
        });
    }

    // 在事务回滚后执行给定的操作，并消费事务结果
    public static <T> void afterRollback(Consumer<T> action, T result) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    action.accept(result);
                }
            }
        });
    }
}

