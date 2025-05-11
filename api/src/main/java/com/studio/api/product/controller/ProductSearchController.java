package com.studio.api.product.controller;


import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.product.service.ProductSearchService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.product.dto.product.GetProductHomeTableResponseDto;
import com.studio.core.product.dto.product.ProductPopularSearchResponseDto;
import com.studio.core.product.repository.ProductQueryJpaRepository;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/api/client/search")
@Tag(name = "Product-search-controller", description = "상품 검색 API 및 인기 검색어 API")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;
    private final ProductQueryJpaRepository productQueryJpaRepository;


    @Operation(summary = "상품 검색", description = "검색어로 상품을 검색합니다.")
    @GetMapping("/product-search")
    public SuccessResponse<Page<GetProductHomeTableResponseDto>> searchProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam String keyword, // 검색어
        Authentication authentication
    ) {
        productSearchService.incrementSearchCount(keyword);
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        Long memberNo =
            (authentication != null) ? getMemberFromAuth(authentication).getMemberNo() : null;
        Page<GetProductHomeTableResponseDto> result = productQueryJpaRepository.getSearchProducts(keyword,memberNo,pageable);
        return SuccessResponse.ok(result);
    }

    @Operation(summary = "인기 검색어 조회", description = "인기 검색어 목록을 조회합니다.")
    @GetMapping("/popular")
    public SuccessResponse<List<ProductPopularSearchResponseDto>> getTopSearchKeywords() {
        return SuccessResponse.ok(productSearchService.getTopSearchKeywords(10));

    }


    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductHomeTableResponseDto.class)))
    })
    @Operation(summary = "검색 -> 추천상품 (베스트 리뷰순)")
    @GetMapping("/recommended/review")
    public SuccessResponse<?> getSortReviewProduct(@RequestParam(defaultValue = "5") int size) {

        return SuccessResponse.ok(productQueryJpaRepository.getSortReviewProduct(size));
    }
}
