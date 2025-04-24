package com.studio.core.global.naver.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NaverProductOrderIdsDto {

    private List<String> productOrderIds;

    public NaverProductOrderIdsDto(List<String> productOrderIds) {
        this.productOrderIds = productOrderIds;
    }
}
