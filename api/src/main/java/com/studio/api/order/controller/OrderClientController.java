package com.studio.api.order.controller;


import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.order.service.OrderService;
import com.studio.core.global.enums.order.search.CancelReturnStateCondition;
import com.studio.core.global.enums.product.naver.CancelReason;
import com.studio.core.global.enums.product.naver.ReturnReason;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.dto.GetClientOrderTableResponseDto;
import com.studio.core.order.dto.GetClientOrderDetailResponseDto;
import com.studio.core.order.dto.GetOrderFormResponseDto;
import com.studio.core.order.repository.OrderQueryJpaRepository;
import com.studio.core.product.repository.CartQueryJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/client/product-order")
@Tag(name = "client-product-order-controller", description = "클라 주문 관리 API")
@RequiredArgsConstructor
public class OrderClientController {

    private final CartQueryJpaRepository cartQueryJpaRepository;

    private final OrderQueryJpaRepository orderQueryJpaRepository;

    private final OrderService orderService;


    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetClientOrderTableResponseDto.class)))
    })
    @Operation(summary = "취소/반품 리스트 조회 API", description = "취소/반품 리스트")
    @GetMapping("/cancel-return")
    public SuccessResponse<?> getCancelOrReturnList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) CancelReturnStateCondition condition,
        Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updateAt"));

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(
            orderQueryJpaRepository.getClientProductOrderCancelOrReturnList(member, pageable,
                condition));

    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetClientOrderTableResponseDto.class)))
    })
    @Operation(summary = "구매내역 리스트 조회 API", description = "구매내역 리스트")
    @GetMapping
    public SuccessResponse<?> getOrderList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.unsorted()
        );
        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(
            orderQueryJpaRepository.getClientOrderList(member, pageable));

    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetClientOrderDetailResponseDto.class)))
    })
    @Operation(summary = "주문상세 조회 API", description = "주문상세")
    @GetMapping("/detail/{orderId}")
    public SuccessResponse<?> getOrderDetail(@PathVariable Long orderId,
        Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(
            orderQueryJpaRepository.getClientOrderDetail(orderId, member));


    }


    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetOrderFormResponseDto.class)))
    })
    @Operation(summary = "주문서 폼")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/form")
    public SuccessResponse<?> getOrderForm(Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(cartQueryJpaRepository.getMemberActiveOrderForm(member));
    }


    @Operation(summary = "주문 상품 취소", description = "<br>주문번호(order no)로 취소<br><br>" +
        "1. 주문관리 > 전체주문내역 > 판매자 취소/반품 중 취소 (티켓 상태 발급전, 주문상태가 결재완료 (TicketState-> NON_ISSUED,ProductOrderState -> PAYED ))<br>"
        +
        "2. 주문관리 > 티켓 발급/취소 관리 > 티켓 발급/취소 > 취소<br>" +
        "취소 사유 <br>" +
        "    INTENT_CHANGED(\"구매 의사 취소\")<br>" +
        "    COLOR_AND_SIZE(\"색상 및 사이즈 변경\")<br>" +
        "    WRONG_ORDER(\"다른 상품 잘못 주문\")<br>" +
        "    PRODUCT_UNSATISFIED(\"서비스 불만족\")<br>" +
        "    INCORRECT_INFO(\"상품 정보 상이\")")
    @Parameter(name = "orderId", description = "취소 요청할 주문번호")
    @Parameter(name = "cancelReason", description = "취소 요청 하는 이유")
    @PostMapping(value = "/{orderId}/cancel/request")
    public SuccessResponse<?> cancel(@PathVariable(value = "orderId") Long orderId,
        @RequestParam(value = "cancelReason") CancelReason cancelReason,
        Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        orderService.cancelRequestOrder(orderId, cancelReason, member);
        return SuccessResponse.ok();

    }


    @Operation(summary = "주문 상품 반품 ", description = "주문번호(order Id)로 반품을 요청 <br>" +
        "구매자 취소/반품 중 반품 요청 (티켓 상태 발급후, 상태가 배송완료 or 구매확정일경우 (TicketState-> AVAILABLE,ProductOrderState -> DELIVERED,PURCHASE_DECIDED ))<br><br>"
        +
        "반품 사유<br>" +
        "    INTENT_CHANGED(\"구매 의사 취소\")<br>" +
        "    COLOR_AND_SIZE(\"색상 및 사이즈 변경\")<br>" +
        "    WRONG_ORDER(\"다른 상품 잘못 주문\")<br>" +
        "    PRODUCT_UNSATISFIED(\"서비스 불만족\")<br>" +
        "    INCORRECT_INFO(\"상품 정보 상이\")<br>" +
        "    WRONG_OPTION(\"색상 등 다른 상품 잘못 배송\")")
    @Parameter(name = "orderId", description = "반품 요청 할 주문번호")
    @Parameter(name = "returnReason", description = "반품 요청 하는 이유")
    @PostMapping(value = "/{orderId}/return/request")
    public SuccessResponse<?> returnOrder(@PathVariable(value = "orderId") Long orderId,
        @RequestParam(value = "returnReason") ReturnReason returnReason,
        Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        orderService.returnRequestOrder(orderId, returnReason, member);
        return SuccessResponse.ok();

    }


    @Operation(summary = "주문 상품 반품요청을 취소", description = "주문번호(order Id)로 반품을 요청한걸 취소 <br>")
    @Parameter(name = "orderId", description = "반품 요청한걸 취소할 할 주문번호")
    @PostMapping(value = "/{orderId}/return/cancel")
    public SuccessResponse<?> returnCancelOrder(@PathVariable(value = "orderId") Long orderId,
        Authentication authentication) {
        MemberAuthEntity member = getMemberFromAuth(authentication);

        orderService.returnCancelOrder(orderId, member);
        return SuccessResponse.ok();

    }

}
