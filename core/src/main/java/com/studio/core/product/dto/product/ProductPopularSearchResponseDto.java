package com.studio.core.product.dto.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductPopularSearchResponseDto {
    private String keyword;
    private int rank;

    public ProductPopularSearchResponseDto(String keyword, int rank) {
        this.keyword = keyword;
        this.rank = rank;
    }

    public static ProductPopularSearchResponseDto from(String keyword, int rank) {
        return ProductPopularSearchResponseDto.builder()
            .keyword(keyword)
            .rank(rank)
            .build();
    }

}