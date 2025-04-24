package com.studio.core.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ProductReviewRequestDto(
    @Schema(description = "리뷰 내용", example = "리뷰 작성합니다아아.~~")
    @Size(min = 10, max = 100)
    @NotBlank
    String content,

    @Min(1)
    @Max(5)
   @Schema(description = "별점")
    int rating,


    @Schema(description = "리뷰 이미지", example = "[\"https://shop-phinf.pstatic.net/20250415_121/1744701009344dwN2u_PNG/78833819491001646_1860902325.png\"," +
        "\"https://shop-phinf.pstatic.net/20250415_293/1744701051585ibKTu_PNG/38639399723298094_1233920217.png\"]")
    @Size(max = 5)
    List<String> ri
) {

}
