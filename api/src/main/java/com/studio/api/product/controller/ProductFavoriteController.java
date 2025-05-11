package com.studio.api.product.controller;


import com.studio.api.product.service.ProductFavoriteService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.product.GetFavoriteProductResponseDto;
import com.studio.core.product.dto.product.GetProductHomeTableResponseDto;
import com.studio.core.product.repository.ProductFavoriteQueryJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.studio.api.global.service.CommonService.getMemberFromAuth;

@RestController
@RequestMapping("/v1/api/client/favorite")
@Tag(name = "Favorite-controller", description = "찜상품 API")
@RequiredArgsConstructor
public class ProductFavoriteController {
    private final ProductFavoriteService favoriteService;
    private final ProductFavoriteQueryJpaRepository favoriteQueryJpaRepository;

    @Operation(summary = "관심상품 등록", description = "버튼으로 상품 찜")
    @PostMapping("/{productId}")
    public SuccessResponse<String> insertFavorite(@PathVariable Long productId,
                                                  Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);
        favoriteService.insertFavorite(productId, member);
        return SuccessResponse.ok("관심상품 등록 완료");
    }

    @Operation(summary = "관심상품 삭제", description = "버튼으로 찜상품 해제")
    @DeleteMapping("/{productId}")
    public SuccessResponse<String> deleteFavorite(@PathVariable Long productId,
                                                  Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        favoriteService.deleteFavorite(productId, member);
        return SuccessResponse.ok("관심상품 삭제 완료");
    }

    @Operation(summary = "상품 관심 상태 확인", description = "이미 찜한 상품인지 확인")
    @GetMapping("/{productId}/status")
    public SuccessResponse<Boolean> isFavorite(@PathVariable Long productId,
                                               Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        boolean result = favoriteService.isFavorite(productId, member);
        return SuccessResponse.ok(result);
    }

    @Operation(summary = "관심상품 목록 조회")
    @GetMapping("/my")
    public SuccessResponse<Page<GetProductHomeTableResponseDto>> getMyFavorites(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<GetProductHomeTableResponseDto> favoriteList = favoriteQueryJpaRepository.findFavoritesByMember(member.getMemberNo(), pageable);

        return SuccessResponse.ok(favoriteList);
    }

}
