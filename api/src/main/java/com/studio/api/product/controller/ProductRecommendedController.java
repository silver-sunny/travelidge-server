package com.studio.api.product.controller;

import com.studio.api.product.service.ProductRecommendedService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.product.dto.product.GetAdminRecommendedProductResponseDto;
import com.studio.core.product.repository.ProductRecommendedQueryJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/api/recommended")
@RequiredArgsConstructor
public class ProductRecommendedController {
    private final ProductRecommendedService productRecommendedService;
    private final ProductRecommendedQueryJpaRepository recommendedQueryJpaRepository;


    @Operation(summary = "관리자 추천 상품 등록", description = "상품 ID로 추천 상품 등록")
    @PostMapping("/{productId}")
    public SuccessResponse<List<GetAdminRecommendedProductResponseDto>> insertRecommendedProduct(@PathVariable Long productId) {
        productRecommendedService.insertRecommendedProduct(productId);
        List<GetAdminRecommendedProductResponseDto> recommendedList = productRecommendedService.getAdminRecommendedProducts();
        return SuccessResponse.ok(recommendedList);
    }
    @Transactional
    @Operation(summary = "관리자 추천 상품 삭제", description = "상품 ID로 추천 상품 삭제")
    @DeleteMapping("/{productId}")
    public SuccessResponse<List<GetAdminRecommendedProductResponseDto>> deleteRecommendedProduct(@PathVariable Long productId) {
        productRecommendedService.deleteRecommendedProduct(productId);
        List<GetAdminRecommendedProductResponseDto> recommendedList = productRecommendedService.getAdminRecommendedProducts();
        return SuccessResponse.ok(recommendedList);
    }

    @Operation(summary = "관리자 추천 상품 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetAdminRecommendedProductResponseDto.class)))
    })
    @GetMapping
    public SuccessResponse<List<GetAdminRecommendedProductResponseDto>> getAdminRecommendedList() {
        return SuccessResponse.ok(productRecommendedService.getAdminRecommendedProducts());
    }

}

