package com.studio.api.product.controller;


import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.product.service.ProductFavoriteService;
import com.studio.api.product.service.ProductService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.product.dto.product.GetProductClientDetailResponseDto;
import com.studio.core.product.dto.product.GetProductHomeTableResponseDto;
import com.studio.core.product.dto.product.GetProductReviewInquiryCountResponseDto;
import com.studio.core.product.repository.ProductQueryJpaRepository;
import com.studio.core.product.repository.ProductRecommendedQueryJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/client/product")
@Tag(name = "product-controller", description = "클라 상품 API")
@RequiredArgsConstructor
public class ProductClientController {

    private final ProductQueryJpaRepository productQueryJpaRepository;
    private final ProductRecommendedQueryJpaRepository recommendedQueryJpaRepository;
    private final ProductService productService;
    private final ProductFavoriteService productFavoriteService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductHomeTableResponseDto.class)))
    })
    @Operation(summary = "더보기 후 상품 리스트", description = "최신순 상품 리스트")
    @GetMapping("/more")
    public SuccessResponse<Page<GetProductHomeTableResponseDto>> getMoreProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size, Authentication authentication) {

        Long memberNo =
            (authentication != null) ? getMemberFromAuth(authentication).getMemberNo() : null;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return SuccessResponse.ok(productQueryJpaRepository.getMoreProducts(memberNo, pageable));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductHomeTableResponseDto.class)))
    })
    @Operation(summary = "홈 > 일반 상품 리스트", description = "기본값 20개, 마감안된순으로 랜덤")
    @GetMapping("/home")
    public SuccessResponse<List<GetProductHomeTableResponseDto>> getHomeProducts(
        Authentication authentication,
        @RequestParam(defaultValue = "20") int size) {
        Long memberNo =
            (authentication != null) ? getMemberFromAuth(authentication).getMemberNo() : null;
        return SuccessResponse.ok(productQueryJpaRepository.getHomeProducts(memberNo, size));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductHomeTableResponseDto.class)))
    })
    @Operation(summary = "홈 > 베스트 상품 ", description = "베스트 상품 리스트<br>" +
        "판매중인 상품,주문 많은순")
    @GetMapping("/best")
    public SuccessResponse<List<GetProductHomeTableResponseDto>> getBestProducts(
        @RequestParam(defaultValue = "20") int size,
        Authentication authentication) {

        Long memberNo =
            (authentication != null) ? getMemberFromAuth(authentication).getMemberNo() : null;
        return SuccessResponse.ok(productQueryJpaRepository.getBestProduct(memberNo, size));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductHomeTableResponseDto.class)))
    })
    @Operation(summary = "홈 > 베스트 상품 더보기 ", description = "베스트 상품 리스트 더보기<br>" +
        "판매중인 상품,주문 많은순")
    @GetMapping("/best/more")
    public SuccessResponse<Page<GetProductHomeTableResponseDto>> getBestProductsMore(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        Authentication authentication) {

        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        Long memberNo =
            (authentication != null) ? getMemberFromAuth(authentication).getMemberNo() : null;
        return SuccessResponse.ok(productQueryJpaRepository.getBestProductMore(memberNo, pageable));
    }


    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductClientDetailResponseDto.class)))
    })
    @Operation(summary = "상품 상세", description = "상품 상세")
    @GetMapping("/{productId}")
    public SuccessResponse<GetProductClientDetailResponseDto> getProductClientDetail(
        @PathVariable Long productId,
        Authentication authentication) {

        Long memberNo =
            (authentication != null) ? getMemberFromAuth(authentication).getMemberNo() : null;

        return SuccessResponse.ok(productQueryJpaRepository.getProduct(productId, memberNo));
    }


    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductReviewInquiryCountResponseDto.class)))
    })
    @Operation(summary = "상품 리뷰, 문의 건수")
    @GetMapping("/review-inquiry-count/{productId}")
    public SuccessResponse<?> getReviewInquiryCount(@PathVariable Long productId) {

        return SuccessResponse.ok(productService.getReviewInquiryCount(productId));

    }

    @Operation(summary = "클라이언트 추천 상품 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductHomeTableResponseDto.class)))
    })
    @GetMapping("/recommended")
    public SuccessResponse<List<GetProductHomeTableResponseDto>> getClientRecommendedList(
        Authentication authentication,
        @RequestParam(value = "size", defaultValue = "20") int size) {

        Long memberNo =
            (authentication != null) ? getMemberFromAuth(authentication).getMemberNo() : null;
        List<GetProductHomeTableResponseDto> recommendedProducts = recommendedQueryJpaRepository.findClientRecommendedProducts(
            memberNo, size);

        return SuccessResponse.ok(recommendedProducts);
    }

    @Operation(summary = "상품의 총 관심 수", description = "해당 상품의 전체 관심상품 수량 조회")
    @GetMapping("/favorite/{productId}/count")
    public SuccessResponse<Long> getFavoriteCount(@PathVariable Long productId) {
        long count = productFavoriteService.getFavoriteCount(productId);
        return SuccessResponse.ok(count);
    }




}
