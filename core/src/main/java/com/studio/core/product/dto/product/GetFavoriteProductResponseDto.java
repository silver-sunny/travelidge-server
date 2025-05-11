package com.studio.core.product.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.utils.CalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "찜 상품 정보")

public class GetFavoriteProductResponseDto {
    @Schema(description = "상품 ID")
    private long productId;
    @Schema(description = "상품명")
    private String productName;
    @Schema(description = "상품가격", hidden = true)
    private Long price;
    @Schema(description = "할인율")
    private long discountRate;

    @Schema(description = "상품 대표 이미지 (Product Representative Image)")
    private String pri;
    @Schema(description = "관심상품여부")
    private boolean isFavorite;

    @QueryProjection
    public GetFavoriteProductResponseDto(Long productId, String productName, Long price, Long discountRate, String pri, boolean isFavorite){
        this.productId = productId;
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
