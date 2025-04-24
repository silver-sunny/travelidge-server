package com.studio.core.product.dto.product.naver;

import com.studio.core.product.dto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "판매자 즉시 할인 정책DTO")
@NoArgsConstructor
public class NaverImmediateDiscountPolicyDto {

    @Schema(description = "PC 할인혜택")
    private NaverDiscountMethodDto discountMethod;


    public NaverImmediateDiscountPolicyDto(ProductRequestDto productDto) {
        this.discountMethod = new NaverDiscountMethodDto(productDto);
    }

}
