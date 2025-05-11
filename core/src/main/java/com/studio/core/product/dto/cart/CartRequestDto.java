package com.studio.core.product.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record CartRequestDto(
        @Schema(description = "상품 아이디", example = "1")
        Long productId,

        @Schema(description = "구매 수량", example = "1")
        int purchaseQuantity,

        @Schema(description = "옵션 직접입력 (예약날짜)", example = "2025-03-01")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식이 아닙니다.")
        String directOption
) {
}
