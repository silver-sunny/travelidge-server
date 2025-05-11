package com.studio.core.global.naver.dto;

import com.studio.core.global.enums.product.naver.NaverProductListSearchOrderType;
import com.studio.core.global.enums.product.naver.NaverProductListSearchPeriodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class NaverProductSearchRequestDto {

    private String fromDate;

    private NaverProductListSearchPeriodType periodType;

    private NaverProductListSearchOrderType orderType;

    private int size;

    private int page;


}
