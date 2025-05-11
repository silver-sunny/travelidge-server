package com.studio.core.product.dto.product;

import com.studio.core.global.enums.ProductState;
import com.studio.core.global.valid.interfaces.PriceValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;


public record ProductRequestDto(
        @Schema(description = "상품명", example = "상품명")
        @Size(max = 100)
        @NotBlank
        String productName,

        @Schema(description = "상품 가격", example = "500000")
        @Min(1000)
        @Max(10000000)
        @PriceValid
        Long price,

        @Schema(description = "할인율", example = "10")
        @Max(99)
        Long discountRate,

        @Schema(description = "재고 수량")
        @Max(1000)
        Long stock,

        @Schema(description = "상품 대표 이미지", example = "https://shop-phinf.pstatic.net/20250415_293/1744701051585ibKTu_PNG/38639399723298094_1233920217.png")
        @NotBlank
        String pri,

        @Schema(description = "상품 상세 이미지", example = "[\"https://shop-phinf.pstatic.net/20250415_293/1744701051585ibKTu_PNG/38639399723298094_1233920217.png\"," +
                "\"https://shop-phinf.pstatic.net/20250415_293/1744701051585ibKTu_PNG/38639399723298094_1233920217.png\"]")
        @Size(max = 5)
        List<String> pdi,

        @Schema(description = "상품 설명", example = "상품 설명")
        @Size(min = 10)
        @NotBlank
        String description,

        @Schema(description = "상품 상태", example = "SALE")
        ProductState productState
) {
}
