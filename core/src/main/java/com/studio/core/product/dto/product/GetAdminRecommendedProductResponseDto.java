package com.studio.core.product.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.product.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "관리자 추천상품 정보")
public class GetAdminRecommendedProductResponseDto {
    @Schema(description = "상품명")
    private String productName;
    @Schema(description = "상품번호")
    private Long productId;

    @QueryProjection
    public GetAdminRecommendedProductResponseDto (String productName, Long productId){
        this.productName = productName;
        this.productId = productId;
    }
    public static GetAdminRecommendedProductResponseDto from(ProductEntity product) {
        return GetAdminRecommendedProductResponseDto.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .build();
    }

}
