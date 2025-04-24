package com.studio.api.product.controller;


import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.product.service.CartService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.cart.CartRequestDto;
import com.studio.core.product.dto.cart.GetCartTableResponseDto;
import com.studio.core.product.repository.CartQueryJpaRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/cart")
@Tag(name = "cart-controller", description = "장바구니 API")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final CartQueryJpaRepository cartQueryJpaRepository;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetCartTableResponseDto.class)))
    })
    @Operation(summary = "전체 주문 리스트")
    @GetMapping
    public SuccessResponse<?> getCarts(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(
            cartQueryJpaRepository.getCarts(pageable, member));

    }

    @Operation(summary = "장바구니 추가 or 결제하기")
    @PostMapping
    public SuccessResponse<Void> upsertCart(@Validated @RequestBody CartRequestDto requestDto,
        Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        cartService.upsertCart(requestDto, member);
        return SuccessResponse.ok();

    }

    @Operation(summary = "장바구니 제거")
    @PostMapping("/{id}")
    public SuccessResponse<?> deleteCart(@PathVariable Long id, Authentication authentication
    ) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        cartService.deleteCart(id,member);
        return SuccessResponse.ok();

    }

}
