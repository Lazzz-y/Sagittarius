package io.github.lazzz.sagittarius.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * 日志拦截器
 *
 * @author Lazzz
 * @date 2025/10/11 17:39
 **/
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* io.github.lazzz.sagittarius..controller..*(..))")
    public void controllerPointcut() {
    }

    /**
     * 执行拦截
     */
    @Around("controllerPointcut()")
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取请求上下文
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 2. 获取追踪信息
        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        // 确保追踪ID不为空
        traceId = StrUtil.isNotBlank(traceId) ? traceId : "N/A";
        spanId = StrUtil.isNotBlank(spanId) ? spanId : "N/A";

        // 3. 获取请求元数据
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        String requestUrl = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        // FIXME 目前是从上游服务获取上游转发的IP，后续如果要获取真实IP需要网关获取并设置到请求头中
        String clientIp = request.getRemoteAddr();

        // 美化参数输出（使用JSON格式化）
        Object[] args = joinPoint.getArgs();
        String reqParam;
        try {
            reqParam = JSON.toJSONString(args);
        } catch (Exception e) {
            reqParam = "参数解析失败: " + e.getMessage();
        }

        // 4. 打印请求开始日志（带分隔线）
        log.info("""
                        
                        ────────────────────────────────────────────────────────────
                        ├─ [请求开始]
                        ├─ 追踪信息: [traceId-{}][spanId-{}]
                        ├─ 请求地址: {} [{}]
                        ├─ 处理方法: {}.{}
                        ├─ 客户端IP: {} [上游服务IP]
                        ├─ 请求参数: {}
                        ────────────────────────────────────────────────────────────""",
                traceId, spanId, requestUrl, httpMethod, className, methodName, clientIp, reqParam);

        // 5. 执行目标方法并计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            // 6. 打印异常日志
            log.error("""
                            
                            ────────────────────────────────────────────────────────────
                            ├─ [请求异常]
                            ├─ 追踪信息: [traceId-{}][spanId-{}]
                            ├─ 处理方法: {}.{}
                            ├─ 异常信息: {}
                            ────────────────────────────────────────────────────────────""",
                    traceId, spanId, className, methodName, e.getMessage(), e);
            throw e;
        } finally {
            stopWatch.stop();
        }

        // 美化响应输出
        String msg;
        try {
            if (result == null) {
                msg = "null";
            } else {
                // 将结果转换为JSON对象
                JSONObject jsonResult = JSONObject.parseObject(JSON.toJSONString(result));
                // 提取msg字段，若不存在则返回"无msg信息"
                msg = jsonResult.getString("msg") != null ? jsonResult.getString("msg") : "N/A";
            }
        } catch (Exception e) {
            msg = "响应解析失败: " + e.getMessage();
        }

        // 7. 打印请求结束日志
        log.info("""
                        
                        ────────────────────────────────────────────────────────────
                        ├─ [请求结束]
                        ├─ 追踪信息: [traceId-{}][spanId-{}]
                        ├─ 处理方法: {}.{}
                        ├─ 响应结果: {}
                        ├─ 耗时统计: {}ms
                        ────────────────────────────────────────────────────────────""",
                traceId, spanId, className, methodName, msg, stopWatch.getTotalTimeMillis());
        return result;
    }
}

