package com.studio.core.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.utils.CalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetOrderFormResponseDto(
        @Schema(description = "장바구니 번호")
        Long cartId,

        @Schema(description = "구매수량")
        int purchaseQuantity,

        @Schema(description = "옵션 직접입력 ( 예약날짜 )")
        String directOption,

        @Schema(description = "상품번호")
        Long productId,

        @Schema(description = "상품 대표 이미지 (Product Representative Image)")
        String pri,

        @Schema(description = "상품 가격")
        Long price,

        @Schema(description = "할인율")
        Long discountRate,

        @Schema(description = "상품명")
        String productName,

        @Schema(description = "상품 상태")
        ProductState productState,

        @Schema(description = "유저 이름")
        String memberName,

        @Schema(description = "유저 핸드폰 번호")
        String memberPhoneNumber
) {

    @QueryProjection
    public GetOrderFormResponseDto {}

    public String getProductStateMeaning(){
        return productState.getMeaning();
    }
    @Schema(description = "총가격")
    public Long getTotalPrice() {
        return price * purchaseQuantity;
    }

    @Schema(description = "할인된 총가격")
    public Long getDiscountTotalPrice() {
        return CalculatorUtil.calculateDiscountPrice(discountRate, price) * purchaseQuantity;
    }

}

