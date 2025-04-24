package com.studio.api.global.config.exception;


import static com.studio.core.global.exception.ErrorCode.FORBIDDEN;

import com.studio.core.global.response.ErrorResponse;
import com.studio.core.global.response.result.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionAdvice {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ErrorResponse<ExceptionResult.ServerErrorData> handleAuthorizationDeniedException(AuthorizationDeniedException e) {

        log.error("[AUTHORIZATION DENIED EXCEPTION] class: [{}], message: [{}]",
                e.getClass().getSimpleName(),
                e.getMessage());

        return ErrorResponse.of(FORBIDDEN.getErrorCode(), FORBIDDEN.getMessage());

    }


}