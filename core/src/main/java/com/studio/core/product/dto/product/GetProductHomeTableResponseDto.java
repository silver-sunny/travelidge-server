package com.studio.core.product.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.utils.CalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // QueryDSL이 기본 생성자가 필요할 수 있음
@Schema(description = "홈 상품")
public class GetProductHomeTableResponseDto {

    @Schema(description = "상품 생성 번호")
    private Long id;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품 가격")
    private Long price;

    @Schema(description = "할인율")
    private Long discountRate;

    @Schema(description = "재고 수량")
    private Long stock;

    @Schema(description = "상품 상태 enum")
    private ProductState productState;

    @Schema(description = "상품 대표 이미지 (Product Representative Image)")
    private String pri;

    @Schema(description = "관심상품여부")
    private Boolean isFavorite;

    @QueryProjection // QueryDSL에서 사용할 생성자
    public GetProductHomeTableResponseDto(Long id, String productName, Long price, Long discountRate, Long stock,
                                          ProductState productState, String pri, Boolean isFavorite) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.discountRate = discountRate;
        this.stock = stock;
        this.productState = productState;
        this.pri = pri;
        this.isFavorite = isFavorite;

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

