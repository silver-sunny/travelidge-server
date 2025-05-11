package com.studio.core.product.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰, 문의 건수")
public record GetProductReviewInquiryCountResponseDto(
        @Schema(description = "리뷰건수")
        int reviewCount,

        @Schema(description = "문의 건수")
        int inquiryCount) {

}