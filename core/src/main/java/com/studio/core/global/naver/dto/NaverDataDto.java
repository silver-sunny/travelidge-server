package com.studio.core.global.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverDataDto {

    private NaverProductOrderDto productOrder;

    private NaverOrderInfoDto order;

    private NaverCancelDto cancel;

    @JsonProperty("return")
    private NaverReturnDto returnDto;

}
