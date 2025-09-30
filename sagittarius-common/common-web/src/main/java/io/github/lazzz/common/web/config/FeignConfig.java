package io.github.lazzz.common.web.config;


import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Enumeration;
import java.util.Set;

/**
 * Feign相关配置类
 *
 * @author Lazzz 
 * @date 2025/09/22 23:57
**/
@Configuration
public class FeignConfig {

    /**
     * 让DispatcherServlet向子线程传递RequestContext
     *
     * @param servlet servlet
     * @return 注册bean
     */
    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet servlet) {
        servlet.setThreadContextInheritable(true);
        return new ServletRegistrationBean<>(servlet, "/**");
    }

    /**
     * 覆写拦截器，在feign发送请求前取出原来的header并转发
     *
     * @return 拦截器
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return (template) -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
                HttpServletRequest request = attributes.getRequest();

                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String value = request.getHeader(name);

                        // 过滤不需要传递的请求头
                        if (shouldForwardHeader(name)) {
                            template.header(name, value);
                        }
                    }
                }
            }
        };
    }

    private boolean shouldForwardHeader(String headerName) {
        // 忽略 content-length
        if ("content-length".equalsIgnoreCase(headerName)) {
            return false;
        }

        // 忽略可能引起路由问题的头信息
        String lowerHeader = headerName.toLowerCase();
        Set<String> ignoredHeaders = Set.of(
                "host",
                "content-length",
                "connection",
                "accept-encoding",
                "user-agent"
        );

        return !ignoredHeaders.contains(lowerHeader);
    }
}

