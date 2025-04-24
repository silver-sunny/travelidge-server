package com.studio.core.product.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.product.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@Schema(description = "상품 검색 결과")
public class ProductSearchResponseDto {
    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품번호")
    private Long productId;

    @Schema(description = "상품 상태")
    private String productState;

    @Schema(description = "상품 채널")
    private String channel;

    @Schema(description = "검색 수")
    private Long searchCount;

    @Schema(description = "상품 설명")
    private String description;

    @Schema(description = "랭킹")
    private Integer rank;

    @QueryProjection
    public ProductSearchResponseDto(String productName, Long productId, String productState, String channel, Long searchCount, String description, Integer rank) {
        this.productName = productName;
        this.productId = productId;
        this.productState = productState;
        this.channel = channel;
        this.searchCount = searchCount;
        this.description = description;
        this.rank = rank;
    }

    public static ProductSearchResponseDto from(ProductEntity product, Long searchCount) {
        return ProductSearchResponseDto.builder()
            .productName(product.getProductName())
            .productId(product.getId())
            .productState(product.getProductState().getMeaning())
            .channel(product.getChannel().getMeaning())
            .searchCount(searchCount)
            .description(product.getDescription())
            .rank(null)
            .build();
    }

}