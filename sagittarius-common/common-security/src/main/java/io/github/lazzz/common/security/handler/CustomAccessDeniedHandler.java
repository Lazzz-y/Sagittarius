package io.github.lazzz.common.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lazzz.sagittarius.common.result.Result;
import io.github.lazzz.sagittarius.common.result.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义权限不足处理器
 *
 * @author Lazzz 
 * @date 2025/09/22 19:55
**/
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), Result.failed(ResultCode.ACCESS_UNAUTHORIZED));
    }
}

