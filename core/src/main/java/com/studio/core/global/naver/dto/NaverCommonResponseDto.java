package com.studio.core.global.naver.dto;

import lombok.Getter;

@Getter
public class NaverCommonResponseDto {

    private String timestamp;

    private NaverSuccessFailProductDataDto data;

    private String code;

    private String traceId;

    private String message;
}
