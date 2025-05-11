package com.studio.core.product.dto.product.naver;

import com.studio.core.product.dto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "상품 고객 혜택 정보 DTO")
@NoArgsConstructor
public class NaverCustomerBenefitDto {

    @Schema(description = "판매자 즉시 할인 정책")
    private NaverImmediateDiscountPolicyDto immediateDiscountPolicy;

    public NaverCustomerBenefitDto(ProductRequestDto productDto) {
        this.immediateDiscountPolicy = new NaverImmediateDiscountPolicyDto(productDto);
    }


}
