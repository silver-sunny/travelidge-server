package com.studio.api.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.global.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Allow-Headers", "*");

        ErrorCode error = ErrorCode.UNAUTHORIZED;
        response.setStatus(error.getHttpCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(error.getErrorCode(), error.getMessage()));
        response.getWriter().write(json);

    }
}
