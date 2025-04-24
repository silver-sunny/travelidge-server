package com.studio.core.product.dto.product.naver;

import com.studio.core.product.dto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "네이버 상품 등록 DTO")
@Getter
@NoArgsConstructor
public class NaverProductDto {


    @Schema(description = "원상품 정보 DTO")
    private NaverOriginProductDto originProduct;

    @Schema(description = "스마트스토어 채널상품 정보 DTO")
    private NaverSmartstoreChannelProductDto smartstoreChannelProduct;


    public NaverProductDto(ProductRequestDto productDto) {
        this.originProduct = new NaverOriginProductDto(productDto);
        this.smartstoreChannelProduct = new NaverSmartstoreChannelProductDto(productDto);
    }

}
