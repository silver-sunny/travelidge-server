package com.studio.core.order.dto.naver;

import com.studio.core.global.naver.dto.NaverSuccessFailProductDataDto;
import lombok.Getter;

@Getter
public class NaverDisPatchProductOrderInfo {

    private String timestamp;

    private NaverSuccessFailProductDataDto data;

    private String traceId;
}
