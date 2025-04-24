package com.studio.api.product.controller;

import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.product.service.NaverProductService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.product.ProductRequestDto;
import com.studio.core.product.dto.product.naver.NaverProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "naver product controller", description = "네이버 상품 API")
@RequestMapping("/v1/api/naver/product")
public class NaverProductController {


    private final NaverProductService naverProductService;

    @Operation(summary = "네이버 상품 등록 API", description = "등록후 24시간 안에 삭제 부탁")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT', 'ROLE_MANAGE')")
    @PostMapping
    public SuccessResponse<NaverProductDto> insertNaverProduct(@RequestBody ProductRequestDto productRequest,
        Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);


        return SuccessResponse.ok(naverProductService.insertNaverProduct(productRequest, member));
    }


    @Operation(summary = "네이버 상품 수정 API", description = "")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT', 'ROLE_MANAGE')")
    @PutMapping("/{productId}")
    public SuccessResponse<NaverProductDto> updateNaverProduct(@PathVariable Long productId,
                                           @RequestBody ProductRequestDto dto) {


        return SuccessResponse.ok(naverProductService.updateNaverProduct(productId, dto));

    }


    @Operation(summary = "네이버 상품 삭제", description = "")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT', 'ROLE_MANAGE')")
    @DeleteMapping(value = "/{productId}")
    public SuccessResponse<Void> deleteNaverProduct(@PathVariable(value = "productId") Long productId) {


        naverProductService.deleteNaverProduct(productId);

        return SuccessResponse.ok();

    }

}
