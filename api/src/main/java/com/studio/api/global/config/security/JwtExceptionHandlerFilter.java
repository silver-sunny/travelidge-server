package com.studio.api.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.global.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Allow-Headers", "*");

        try {
            filterChain.doFilter(request, response);
        }catch (CustomException e){
            jwtExceptionHandler(response, e.getErrorCode());
        }catch (Exception e){
            jwtExceptionHandler(response, ErrorCode.SERVER_UNTRACKED_ERROR);
        }


    }

    public void jwtExceptionHandler(HttpServletResponse response, ErrorCode error) {
        response.setStatus(error.getHttpCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(error.getErrorCode(), error.getMessage()));
            response.getWriter().write(json);
        } catch (Exception e){

        }
    }
}
