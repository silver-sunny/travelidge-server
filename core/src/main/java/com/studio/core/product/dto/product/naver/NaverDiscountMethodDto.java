package com.studio.core.product.dto.product.naver;

import com.studio.core.product.dto.product.ProductRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "할인 혜택")
@NoArgsConstructor
public class NaverDiscountMethodDto {

    @Schema(description = "할인값", example = "10")
    private Long value;

    @Schema(description = "할인 단위", example = "PERCENT")
    private String unitType;

    public NaverDiscountMethodDto(ProductRequestDto productDto) {
        this.value = productDto.discountRate();
        this.unitType = "PERCENT";
    }


}
