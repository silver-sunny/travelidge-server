package com.studio.core.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private Exception originException;
    private String addMessage = null;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.name()); // 기본적으로 에러 코드명을 메시지로 설정
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String addMessage){
        super(addMessage); // 🔥 여기 추가
        this.errorCode = errorCode;
        this.addMessage = addMessage;
    }

    public CustomException(ErrorCode errorCode, Exception exception) {
        super(exception); // 🔥 여기 추가
        this.errorCode = errorCode;
        this.originException = exception;
    }
}
