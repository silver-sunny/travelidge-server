package com.studio.core.global.naver.dto;

import lombok.Getter;


@Getter
public class NaverOrderStatusDto {

    private String timestamp;

    private NaverOrderStatusDataDto data;

    private String traceId;


}
