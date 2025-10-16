package io.github.lazzz.sagittarius.common.web.aspect;


import cn.hutool.core.util.StrUtil;
import io.github.lazzz.common.security.util.SecurityUtils;
import io.github.lazzz.sagittarius.common.web.annotation.PreventDuplicateResubmit;
import io.github.lazzz.sagittarius.common.web.exception.BizException;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * 防止重复提交切面
 *
 * @author Lazzz 
 * @date 2025/09/22 22:49
**/
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DuplicateSubmitAspect {

    private final RedissonClient redissonClient;

    @Pointcut("@annotation(preventDuplicateResubmit)")
    public void preventDuplicateResubmitCut(PreventDuplicateResubmit preventDuplicateResubmit){
        log.info("定义防重复提交切点");
    }

    @Around(value = "preventDuplicateResubmitCut(preventDuplicateResubmit)", argNames = "joinPoint,preventDuplicateResubmit")
    public Object doAround(ProceedingJoinPoint joinPoint, PreventDuplicateResubmit preventDuplicateResubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String jti = SecurityUtils.getJti();
        if (StrUtil.isNotBlank(jti)) {
            String resubmitLockKey = CacheConstants.RESUBMIT_LOCK_PREFIX + jti + ":" + request.getMethod() + "-" + request.getRequestURI();
            // 防止重复提交过期时间
            int expire = preventDuplicateResubmit.expire();
            RLock lock = redissonClient.getLock(resubmitLockKey);
            // 获取锁,失败直接返回 false
            boolean lockResult = lock.tryLock(0, expire, TimeUnit.SECONDS);
            if (!lockResult){
                throw new BizException(ResultCode.REPEAT_SUBMIT_ERROR);
            }
        }
        return joinPoint.proceed();
    }

}

