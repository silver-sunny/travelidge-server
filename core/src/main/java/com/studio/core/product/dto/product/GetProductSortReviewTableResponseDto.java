package com.studio.core.product.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.utils.CalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 상품")
public record GetProductSortReviewTableResponseDto(

    @Schema(description = "상품 생성 번호")
    Long id,

    @Schema(description = "상품명")
    String productName,

    @Schema(description = "상품 가격")
    Long price,

    @Schema(description = "할인율")
    Long discountRate,

    @Schema(description = "상품 상태 enum")
    ProductState productState,

    @Schema(description = "상품 상태 enum")
    String pri,

    @Schema(description = "상품 리뷰 평균(소수점 1개까지만)")
    Double averageRate
) {


    @QueryProjection
    public GetProductSortReviewTableResponseDto {

    }

    @Schema(description = "상품 상태")
    public String getProductStateName() {
        return productState.getMeaning();
    }

    @Schema(description = "할인가")
    public Long getDiscountPrice() {
        return CalculatorUtil.calculateDiscountPrice(discountRate, price);
    }


}

