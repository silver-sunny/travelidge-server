package com.studio.core.global.exception;


import static com.studio.core.global.exception.ErrorCode.ORACLE_BUCKET_FAIL;
import static com.studio.core.global.exception.ErrorCode.ORACLE_BUCKET_NOT_EXIST;
import static com.studio.core.global.exception.ErrorCode.PARAMETER_GRAMMAR_ERROR;
import static com.studio.core.global.exception.ErrorCode.PARAMETER_VALIDATION_ERROR;
import static com.studio.core.global.exception.ErrorCode.SERVER_UNTRACKED_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.oracle.bmc.model.BmcException;
import com.studio.core.global.response.ErrorResponse;
import com.studio.core.global.response.result.ExceptionResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {


    /**
     * 등록되지 않은 예외
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse<ExceptionResult.ServerErrorData> handleUntrackedException(Exception e) {
        e.printStackTrace();
        log.error("[UNTRACKED ERROR] class: [{}], message: [{}]",
            e.getClass().getSimpleName(),
            e.getMessage());
        String errorLocation;
        try {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
            errorLocation =
                stackTraceElement.getClassName() + " > " + stackTraceElement.getMethodName() + " > "
                    + stackTraceElement.getLineNumber();
            for (StackTraceElement traceElement : e.getStackTrace()) {
                System.out.println(traceElement.getClassName());
                if (traceElement.getClassName().contains("ticket")) {
                    errorLocation += "  ##  " + traceElement.getClassName() + " > "
                        + traceElement.getMethodName() + " > " + traceElement.getLineNumber();
                    break;
                }
            }

        } catch (Exception error) {
            errorLocation = null;
        }
        ExceptionResult.ServerErrorData serverErrorData = ExceptionResult.ServerErrorData.builder()
            .errorClass(e.getClass().toString())
            .errorMessage(e.getMessage())
            .errorLocation(errorLocation)
            .build();
        return ErrorResponse.of(SERVER_UNTRACKED_ERROR.getErrorCode(),
            SERVER_UNTRACKED_ERROR.getMessage(), serverErrorData);
    }

    /**
     * 파라미터 검증 예외
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ErrorResponse<List<ExceptionResult.ParameterData>> handleValidationExceptions(
        MethodArgumentNotValidException e) {

        log.error("[PARAMETER VALIDATION EXCEPTION] class: [{}], message: [{}]",
            e.getClass().getSimpleName(),
            e.getMessage());

        List<ExceptionResult.ParameterData> list = new ArrayList<>();

        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            ExceptionResult.ParameterData parameterData = ExceptionResult.ParameterData.builder()
                .key(fieldError.getField())
                .value(fieldError.getRejectedValue() == null ? null
                    : fieldError.getRejectedValue().toString())
                .reason(fieldError.getDefaultMessage())
                .build();
            list.add(parameterData);
        }

        return ErrorResponse.of(PARAMETER_VALIDATION_ERROR.getErrorCode(),
            PARAMETER_VALIDATION_ERROR.getMessage(), list);
    }

//    @ExceptionHandler(AuthorizationDeniedException.class)
//    public ErrorResponse<ExceptionResult.ServerErrorData> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
//
//        log.error("[AUTHORIZATION DENIED EXCEPTION] class: [{}], message: [{}]",
//                e.getClass().getSimpleName(),
//                e.getMessage());
//
//        return ErrorResponse.of(FORBIDDEN.getErrorCode(), FORBIDDEN.getMessage());
//
//    }


    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ErrorResponse<List<ExceptionResult.ParameterData>> handleConstraintViolationExceptions(
        ConstraintViolationException e) {

        log.warn("[PARAMETER VALIDATION EXCEPTION] class: [{}], message: [{}]",
            e.getClass().getSimpleName(),
            e.getMessage());

        List<ExceptionResult.ParameterData> list = new ArrayList<>();

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString(); // 필드 경로
            String message = violation.getMessage(); // 에러 메시지
            Object invalidValue = violation.getInvalidValue(); // 잘못된 값

            ExceptionResult.ParameterData parameterData = ExceptionResult.ParameterData.builder()
                .key(propertyPath) // 예외가 발생한 필드명
                .value(invalidValue == null ? null : invalidValue.toString()) // 잘못된 입력값
                .reason(message) // 에러 메시지
                .build();
            list.add(parameterData);
        }

        return ErrorResponse.of(PARAMETER_VALIDATION_ERROR.getErrorCode(),
            PARAMETER_VALIDATION_ERROR.getMessage(), list);
    }


    /**
     * 지원되지 않는 HTTP 메서드 예외 처리
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse<String> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {

        log.warn("[NOT FOUND] class: [{}], message: [{}]",
            e.getClass().getSimpleName(),
            e.getMessage());

        return ErrorResponse.of(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(), e.getMessage());
    }

    /**
     * 파라미터 문법 예외
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ErrorResponse<String> handleHttpMessageParsingExceptions(
        HttpMessageNotReadableException e) {

        log.warn("[PARAMETER GRAMMAR EXCEPTION] class: [{}], message: [{}]",
            e.getClass().getSimpleName(),
            e.getMessage());

        return ErrorResponse.of(PARAMETER_GRAMMAR_ERROR.getErrorCode(),
            PARAMETER_GRAMMAR_ERROR.getMessage(), e.getMessage());
    }


    /**
     * 커스텀 예외
     */
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleCustomExceptions(CustomException e) {

        if (e.getOriginException() != null) {
            log.error("[ORIGIN ERROR] class: [{}], message: [{}], ",
                e.getOriginException().getClass().getSimpleName(),
                e.getOriginException().getMessage());
        }

        log.warn("[CUSTOM EXCEPTION] class: [{}], message: [{}]",
            e.getClass().getSimpleName(),
            e.getErrorCode().getMessage());

        ErrorResponse<Object> body = ErrorResponse.of(e.getErrorCode().getErrorCode(),
            e.getErrorCode().getMessage());
        if (e.getAddMessage() != null) {
            body = ErrorResponse.of(e.getErrorCode().getErrorCode(), e.getErrorCode().getMessage(),
                e.getAddMessage());
        }
        return new ResponseEntity<>(body, HttpStatus.valueOf(e.getErrorCode().getHttpCode()));
    }
    /**
     * OCI BmcException 예외 처리
     */
    /**
     * OCI BmcException 예외 처리
     */
    @ExceptionHandler(BmcException.class)
    public ResponseEntity<ErrorResponse<?>> handleBmcException(BmcException e) {

        log.error("[OCI BMC EXCEPTION] class: [{}], message: [{}], statusCode: [{}]",
            e.getClass().getSimpleName(),
            e.getMessage(),
            e.getStatusCode());

        // 🔸 404 + ObjectNotFound 는 따로 처리
        if (e.getStatusCode() == 404 && "ObjectNotFound".equals(e.getServiceCode())) {
            ErrorResponse<?> body = ErrorResponse.of(
                ORACLE_BUCKET_NOT_EXIST.getErrorCode(),
                ORACLE_BUCKET_NOT_EXIST.getMessage(),
                e.getMessage()
            );
            return ResponseEntity.status(ORACLE_BUCKET_NOT_EXIST.getHttpCode()).body(body);  // 🔥 여기
        }

        // 🔹 그 외 BmcException은 공통 처리
        String errorLocation;
        try {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
            errorLocation = stackTraceElement.getClassName() + " > " + stackTraceElement.getMethodName() + " > " + stackTraceElement.getLineNumber();
            for (StackTraceElement traceElement : e.getStackTrace()) {
                if (traceElement.getClassName().contains("ticket")) {
                    errorLocation += "  ##  " + traceElement.getClassName() + " > " + traceElement.getMethodName() + " > " + traceElement.getLineNumber();
                    break;
                }
            }
        } catch (Exception error) {
            errorLocation = null;
        }

        ExceptionResult.ServerErrorData serverErrorData = ExceptionResult.ServerErrorData.builder()
            .errorClass(e.getClass().toString())
            .errorMessage(e.getMessage())
            .errorLocation(errorLocation)
            .build();

        ErrorResponse<?> body = ErrorResponse.of(
            ORACLE_BUCKET_FAIL.getErrorCode(),
            ORACLE_BUCKET_FAIL.getMessage(),
            serverErrorData
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);  // 🔥 여기
    }



}