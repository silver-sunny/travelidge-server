package com.studio.api.global.config.swagger;

import com.studio.core.global.exception.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.studio.core.global.exception.ErrorCode.*;


@Getter
public enum SwaggerResponseDescription {
    BASIC_ERROR_CODE(new LinkedHashSet<>()),

    ADMIN_LOGIN(new LinkedHashSet<>(Set.of(
            ADMIN_PASSWORD_MISMATCH

    ))),

    PROVIDE_REFRESH_TOKEN(new LinkedHashSet<>(Set.of(
            JWT_NOT_EXPIRE_TOKEN,
            JWT_ERROR_TOKEN,
            JWT_UNMATCHED_CLAIMS
    ))),
    ADMIN_REGISTER(new LinkedHashSet<>(Set.of(
            ADMIN_ALREADY_ID
    )));






    private Set<ErrorCode> errorCodeList;

    SwaggerResponseDescription(Set<ErrorCode> errorCodeList) {
        // 공통 에러
        errorCodeList.addAll(new LinkedHashSet<>(Set.of(
                SERVER_UNTRACKED_ERROR,
                INVALID_PARAMETER,
                PARAMETER_VALIDATION_ERROR,
                PARAMETER_GRAMMAR_ERROR,
                UNAUTHORIZED,
                FORBIDDEN,
                JWT_ERROR_TOKEN,
                JWT_EXPIRE_TOKEN,
                AUTHORIZED_ERROR,
                OBJECT_NOT_FOUND
        )));

        this.errorCodeList = errorCodeList;
    }
}