package com.studio.core.global.naver.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NaverProductOrderInfo {

    private String timestamp;

    private List<NaverDataDto> data;

    private String traceId;
}
