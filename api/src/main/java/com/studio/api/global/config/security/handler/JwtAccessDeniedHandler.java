package com.studio.api.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.global.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Allow-Headers", "*");


        ErrorCode error = ErrorCode.FORBIDDEN;
        response.setStatus(error.getHttpCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(error.getErrorCode(), error.getMessage()));
        response.getWriter().write(json);
    }
}
