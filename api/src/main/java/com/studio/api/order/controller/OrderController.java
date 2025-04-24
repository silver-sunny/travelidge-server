package com.studio.api.order.controller;

import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.order.service.OrderService;

import com.studio.core.global.enums.product.naver.CancelReason;
import com.studio.core.global.enums.product.naver.ReturnReason;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.dto.AllProductOrdersSearchDto;
import com.studio.core.order.dto.GetProductOrderCancelOrReturnCompleteTableResponseDto;
import com.studio.core.order.dto.GetProductOrderCancelOrReturnRequestTableResponseDto;
import com.studio.core.order.dto.GetProductOrderNonTicketTableResponseDto;
import com.studio.core.order.dto.GetProductOrderSummaryDto;
import com.studio.core.order.dto.GetProductOrderTableResponseDto;
import com.studio.core.order.dto.GetReturnCountResponse;
import com.studio.core.order.dto.ProductOrdersNonTicketSearchDto;
import com.studio.core.order.dto.ProductOrdersReturnCompleteSearchDto;
import com.studio.core.order.dto.ProductOrdersReturnRequestSearchDto;
import com.studio.core.order.repository.OrderQueryJpaRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/product-order")
@Tag(name = "product-order-controller", description = "주문 관리 API")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderQueryJpaRepository orderQueryJpaRepository;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductOrderTableResponseDto.class)))
    })
    @Operation(summary = "전체 주문 리스트")
    @GetMapping
    public SuccessResponse<?> productOrders(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @ModelAttribute AllProductOrdersSearchDto searchDto) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        return SuccessResponse.ok(orderQueryJpaRepository.getProductOrderList(pageable, searchDto));


    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductOrderNonTicketTableResponseDto.class)))
    })
    @Operation(summary = "미발급티켓 리스트 조회 API", description = "[티켓 발급/취소 관리] 페이지 구현에 사용")
    @GetMapping("/none-tickets")
    public SuccessResponse<?> getProductOrderNonTicketList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @ModelAttribute ProductOrdersNonTicketSearchDto searchDto) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        return SuccessResponse.ok(
            orderQueryJpaRepository.getProductOrderNonTicketList(pageable, searchDto));

    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductOrderCancelOrReturnRequestTableResponseDto.class)))
    })
    @Operation(summary = "취소/반품 요청 리스트 조회 API", description = "취소/반품 요청 리스트")
    @GetMapping("/cancel-return-request")
    public SuccessResponse<?> getProductOrderCancelOrReturnRequestList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @ModelAttribute ProductOrdersReturnRequestSearchDto searchDto) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        return SuccessResponse.ok(
            orderQueryJpaRepository.getProductOrderCancelOrReturnRequestList(pageable, searchDto));


    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductOrderCancelOrReturnCompleteTableResponseDto.class)))
    })
    @Operation(summary = "취소/반품 완료 리스트 조회 API", description = "취소/반품 완료 리스트")
    @GetMapping("/cancel-return-complete")
    public SuccessResponse<?> getProductOrderCancelOrReturnCompleteList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @ModelAttribute ProductOrdersReturnCompleteSearchDto searchDto) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        return SuccessResponse.ok(
            orderQueryJpaRepository.getProductOrderCancelOrReturnCompleteList(pageable, searchDto));

    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetReturnCountResponse.class)))
    })
    @Operation(summary = "취소/반품 요청,완료 건수 API")
    @GetMapping("/return-count")
    public SuccessResponse<?> getReturnCount() {

        return SuccessResponse.ok(orderService.getReturnCount());

    }


    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetProductOrderSummaryDto.class)))
    })
    @Operation(summary = "주문 정보 요약 조회 API")
    @Parameter(name = "orderId", description = "주문번호")
    @GetMapping("/summary/{orderId}")
    public SuccessResponse<?> getOrderSummary(@PathVariable(value = "orderId") Long orderId) {

        return SuccessResponse.ok(orderQueryJpaRepository.getProductOrderSummary(orderId));
    }


    @Operation(summary = "주문 상품 반품 ", description = "주문번호(order Id)로 반품을 요청 -> 승인합니다. <br>" +
        "주문관리 > 전체주문내역 > 판매자 취소/반품 중 반품 (티켓 상태 발급후, 상태가 배송완료 or 구매확정일경우 (TicketState-> AVAILABLE,ProductOrderState -> DELIVERED,PURCHASE_DECIDED ))<br><br>"
        +
        "반품 사유<br>" +
        "    INTENT_CHANGED(\"구매 의사 취소\")<br>" +
        "    COLOR_AND_SIZE(\"색상 및 사이즈 변경\")<br>" +
        "    WRONG_ORDER(\"다른 상품 잘못 주문\")<br>" +
        "    PRODUCT_UNSATISFIED(\"서비스 불만족\")<br>" +
        "    INCORRECT_INFO(\"상품 정보 상이\")<br>" +
        "    WRONG_OPTION(\"색상 등 다른 상품 잘못 배송\")")
    @Parameter(name = "orderId", description = "반품 요청 -> 승인 할 주문번호")
    @Parameter(name = "returnReason", description = "반품 요청 -> 승인 할 주문번호")
    @PostMapping(value = "/{orderId}/return/request-approve")
    public SuccessResponse<?> returnOrder(@PathVariable(value = "orderId") Long orderId,
        @RequestParam(value = "returnReason") ReturnReason returnReason) {

        orderService.returnOrder(orderId, returnReason);
        return SuccessResponse.ok();


    }

    @Operation(summary = "주문 상품 반품 승인", description = "주문번호(order id)로 반품을 승인합니다.<br>" +
        "주문관리 > 반품관리 > 반품요청 > 취소/반품 > 승인 (반품 상태가 반품 요청일경우 가능 (CancelOrReturnState-> CANCEL_REQUEST or RETURN_REQUEST))")
    @Parameter(name = "orderId", description = "반품 승인할 주문번호")
    @PostMapping(value = "/{orderId}/cancel-return/approve")
    public SuccessResponse<?> returnApprove(@PathVariable(value = "orderId") Long orderId,
        Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);


        orderService.returnApprove(orderId,member);
        return SuccessResponse.ok();

    }


    @Operation(summary = "주문 상품 반품 거부(철회)", description = "주문번호(order id)로 반품 거부(철회).<br>" +
        "주문 관리 > 취소 반품 관리 > 취소/반품 요청 > 취소/반품 > 거부 (반품만 거부 가능, 취소는 거부 불가능(CancelOrReturnState -> RETURN_REQUEST))")
    @Parameter(name = "orderId", description = "반품 거부할 주문번호")
    @Parameter(name = "rejectReturnReason", description = "반품 거부하는 이유")
    @PostMapping(value = "/{orderId}/return/reject")
    public SuccessResponse<?> returnRejectOrder(@PathVariable(value = "orderId") Long orderId,
        @RequestParam(value = "rejectReturnReason") String rejectReturnReason) {



        orderService.returnRejectOrder(orderId, rejectReturnReason);
        return SuccessResponse.ok();


    }


    @Operation(summary = "주문 상품 취소", description = "<br>주문번호(order no)로 취소요청<br><br>" +
        "1. 주문관리 > 전체주문내역 > 판매자 취소/반품 중 취소 (티켓 상태 발급전, 주문상태가 결재완료 (TicketState-> NON_ISSUED,ProductOrderState -> PAYED ))<br>"
        +
        "2. 주문관리 > 티켓 발급/취소 관리 > 티켓 발급/취소 > 취소<br>" +
        "취소 사유 <br>" +
        "    INTENT_CHANGED(\"구매 의사 취소\")<br>" +
        "    COLOR_AND_SIZE(\"색상 및 사이즈 변경\")<br>" +
        "    WRONG_ORDER(\"다른 상품 잘못 주문\")<br>" +
        "    PRODUCT_UNSATISFIED(\"서비스 불만족\")<br>" +
        "    INCORRECT_INFO(\"상품 정보 상이\")")
    @Parameter(name = "orderId", description = "취소 요청 승인할 주문번호")
    @Parameter(name = "cancelReason", description = "취소 요청 하는 이유")
    @PostMapping(value = "/{orderId}/cancel/request")
    public SuccessResponse<?> cancel(@PathVariable(value = "orderId") Long orderId,
        @RequestParam(value = "cancelReason") CancelReason cancelReason) {



        orderService.cancelOrder(orderId, cancelReason);
        return SuccessResponse.ok();


    }
//    @Operation(summary = "주문 반품 철회", description = "주문 반품 철회")
//    @PutMapping("/{orderId}/return/reject")
//    public ResponseEntity<?> returnRejectOrder(@PathVariable(value = "orderId") Long orderId,
//                                               @RequestParam(value = "rejectReturnReason") String rejectReturnReason) {
//        productOrderService.returnRejectOrder(orderId, rejectReturnReason);
//        return ApiResponse.toResponseEntity();
//
//    }
}
