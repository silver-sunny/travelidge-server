package com.studio.core.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductReviewRequestDto(
    @Schema(description = "리뷰 내용", example = "리뷰 작성합니다아아.~~")
    @Size(min = 10, max = 100)
    @NotBlank
    String content,

    @Min(1)
    @Max(5)
   @Schema(description = "별점")
    int rating
) {

}
