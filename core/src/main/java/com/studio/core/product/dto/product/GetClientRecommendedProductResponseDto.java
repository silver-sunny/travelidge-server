package com.studio.core.product.dto.product;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.utils.CalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "클라이언트 추천상품 정보")

public class GetClientRecommendedProductResponseDto {
    @Schema(description = "상품 ID")
    private Long id;
    @Schema(description = "상품명")
    private String productName;
    @Schema(description = "상품가격", hidden = true)
    private Long price;
    @Schema(description = "할인율")
    private Long discountRate;
    @Schema(description = "상품 대표 이미지 (Product Representative Image)")
    private String pri;
    @Schema(description = "관심상품여부")
    private Boolean isFavorite;

    @QueryProjection
    public GetClientRecommendedProductResponseDto(Long id, String productName, Long price, Long discountRate, String pri, Boolean isFavorite){
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.discountRate = discountRate;
        this.pri = pri;
        this.isFavorite = isFavorite;
    }

    @Schema(description = "할인가")
    public Long getDiscountPrice() {
        return CalculatorUtil.calculateDiscountPrice(discountRate, price);
    }

}
