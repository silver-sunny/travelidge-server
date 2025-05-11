package com.studio.core.global.naver.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NaverSuccessFailProductDataDto {

    private List<String> successProductOrderIds;

    private List<NaverFailProductOrderInfosDto> failProductOrderInfos;

}
