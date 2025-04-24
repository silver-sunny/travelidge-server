package com.studio.api.review.controller;

import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.review.service.ReviewService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.dto.GetClientOrderTableResponseDto;
import com.studio.core.review.dto.GetClientMyProductReviewTableResponseDto;
import com.studio.core.review.dto.GetClientProductReviewTableResponseDto;
import com.studio.core.review.dto.ProductReviewRequestDto;
import com.studio.core.review.repository.ReviewQueryJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/client/review")
@Tag(name = "review-controller", description = "리뷰 관리 API")
@RequiredArgsConstructor
public class ReviewClientController {

    private final ReviewService reviewService;

    private final ReviewQueryJpaRepository reviewQueryJpaRepository;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetClientProductReviewTableResponseDto.class)))
    })
    @Operation(summary = "상품 리뷰 리스트")
    @GetMapping("/{productId}")
    public SuccessResponse<?> getProductReviewList(
        @PathVariable Long productId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        return SuccessResponse.ok(
            reviewQueryJpaRepository.getClientProductReviews(productId, pageable));


    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetClientMyProductReviewTableResponseDto.class)))
    })
    @Operation(summary = "나의 리뷰 리스트(리뷰 완료)")
    @GetMapping("/my")
    public SuccessResponse<?> getMyProductReviewList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(
            reviewQueryJpaRepository.getClientMyProductReviews(member, pageable));


    }


    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetClientOrderTableResponseDto.class)))
    })
    @Operation(summary = "작성가능한 주문")
    @GetMapping("/my/unwritten")
    public SuccessResponse<?> getMyUnwrittenProductReviewList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(
            reviewQueryJpaRepository.getMyUnwrittenProductReviewList(member, pageable));


    }


    @Operation(summary = "리뷰 작성 하기")
    @PostMapping("/{orderId}")
    public SuccessResponse<?> insertReview(@PathVariable Long orderId,
        @Validated @RequestBody ProductReviewRequestDto requestDto,
        Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        reviewService.insertReview(orderId, requestDto, member);
        return SuccessResponse.ok();

    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    public SuccessResponse<?> deleteReview(@PathVariable Long reviewId,
        Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        reviewService.deleteReview(reviewId,  member);
        return SuccessResponse.ok();

    }

}
