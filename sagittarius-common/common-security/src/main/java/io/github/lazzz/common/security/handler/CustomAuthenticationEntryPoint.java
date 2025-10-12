package io.github.lazzz.common.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lazzz.sagittarius.common.result.Result;
import io.github.lazzz.sagittarius.common.result.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义认证失败处理
 *
 * @author Lazzz 
 * @date 2025/09/22 19:56
**/
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        mapper.writeValue(response.getOutputStream(), Result.failed(ResultCode.TOKEN_INVALID));
    }
}

