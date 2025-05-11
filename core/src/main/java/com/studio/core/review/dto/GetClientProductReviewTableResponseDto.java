package com.studio.core.review.dto;


import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "상품 리뷰 ")
public record GetClientProductReviewTableResponseDto(

    @Schema(description = "리뷰 번호")
    Long id,

    @Schema(description = "리뷰내용")
    String content,

    @Schema(description = "리뷰내용")
    int rating,

    @Schema(description = "리뷰 이미지")
    List<String> ri,


    @Schema(description = "리뷰 작성한 날짜")
    LocalDateTime createAt,

    @Schema(description = "리뷰 작성자 닉네임")
    String memberNickname,

    @Schema(description = "리뷰 작성자 번호")
    Long memberNo


) {


    @QueryProjection
    public GetClientProductReviewTableResponseDto {
    }
}

