package com.studio.api.order.controller;



import com.studio.api.order.service.NaverOrderService;
import com.studio.core.global.enums.product.naver.CancelReason;
import com.studio.core.global.enums.product.naver.ReturnReason;
import com.studio.core.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "NAVER product state", description = "네이버 상품 상태 관리 API")
@RequiredArgsConstructor
@RequestMapping("/v1/api/naver/product-order")
public class NaverOrderStateController {

    private final NaverOrderService naverOrderService;

    @Operation(summary = "네이버 주문 상품 반품 ", description = "주문번호(order Id)로 반품을 요청 -> 승인합니다. <br>" +
            "주문관리 > 전체주문내역 > 판매자 취소/반품 중 반품 (티켓 상태 발급후, 상태가 배송완료 or 구매확정일경우 (TicketState-> AVAILABLE,ProductOrderState -> DELIVERED,PURCHASE_DECIDED ))<br><br>" +
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
    public SuccessResponse<?> naverProductReturnRequestApprove(@PathVariable(value = "orderId") Long orderId,
                                                              @RequestParam(value = "returnReason") ReturnReason returnReason) {

        naverOrderService.naverReturnRequestAndApprove(orderId, returnReason);
        return SuccessResponse.ok();


    }

    @Operation(summary = "네이버 주문 상품 취소/반품 승인", description = "주문번호(order id)로 취소/반품을 승인합니다.<br>" +
            "주문관리 > 취소/반품관리 > 취소/반품요청 > 취소/반품 > 승인 (취소/반품 상태가 취소,반품 요청일경우 가능 (CancelOrReturnState-> CANCEL_REQUEST or RETURN_REQUEST))")
    @Parameter(name = "orderId", description = "반품 승인할 주문번호")
    @PostMapping(value = "/{orderId}/cancel-return/approve")
    public SuccessResponse<?> naverProductCancelOrReturnApprove(@PathVariable(value = "orderId") Long orderId) {

        naverOrderService.naverCancelOrReturnApprove(orderId);
        return SuccessResponse.ok();

    }


    @Operation(summary = "네이버 주문 상품 반품 거부(철회)", description = "주문번호(order id)로 반품 거부(철회).<br>" +
            "주문 관리 > 취소 반품 관리 > 취소/반품 요청 > 취소/반품 > 거부 (반품만 거부 가능, 취소는 거부 불가능(CancelOrReturnState -> RETURN_REQUEST))")
    @Parameter(name = "orderId", description = "반품 거부할 주문번호")
    @Parameter(name = "rejectReturnReason", description = "반품 거부하는 이유")
    @PostMapping(value = "/{orderId}/return/reject")
    public SuccessResponse<?> naverProductReturnReject(@PathVariable(value = "orderId") Long orderId,
                                                      @RequestParam(value = "rejectReturnReason") String rejectReturnReason) {

        naverOrderService.naverReturnReject(orderId, rejectReturnReason);
        return SuccessResponse.ok();


    }




    @Operation(summary = "네이버 주문 상품 취소", description = "<br>주문번호(order no)로 취소요청<br><br>" +
            "1. 주문관리 > 전체주문내역 > 판매자 취소/반품 중 취소 (티켓 상태 발급전, 주문상태가 결재완료 (TicketState-> NON_ISSUED,ProductOrderState -> PAYED ))<br>" +
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
    public SuccessResponse<?> naverProductCancelRequest(@PathVariable(value = "orderId") Long orderId,
                                                       @RequestParam(value = "cancelReason") CancelReason cancelReason) {

        naverOrderService.naverCancelRequest(orderId, cancelReason);
        return SuccessResponse.ok();


    }
//
//
//    @Operation(summary = "네이버 상품 발주 / 발송 처리 ", description = "발주 / 발송 처리 ")
//    @PostMapping(value = "/{orderId}/dispatch")
//    public ResponseEntity<?> naverProductDispatch(@PathVariable(value = "orderId") Long orderId) {
//
//        naverProductStateDao.dispatchNaverProduct(orderId);
//        return ApiResponse.toResponseEntity();
//
//    }



//    @Operation(summary = "네이버 상품 반품 요청", description =
//            "주문번호(order id)로 반품 요청<br><br>" +
//                    "반품 사유<br>" +
//                    "    INTENT_CHANGED(\"구매 의사 취소\")<br>" +
//                    "    COLOR_AND_SIZE(\"색상 및 사이즈 변경\")<br>" +
//                    "    WRONG_ORDER(\"다른 상품 잘못 주문\")<br>" +
//                    "    PRODUCT_UNSATISFIED(\"서비스 불만족\")<br>" +
//                    "    INCORRECT_INFO(\"상품 정보 상이\")<br>" +
//                    "    WRONG_OPTION(\"색상 등 다른 상품 잘못 배송\")")
//    @Parameter(name = "orderId", description = "반품 요철할 주문번호")
//    @Parameter(name = "returnReason", description = "반품 요청하는 이유")
//    @PostMapping(value = "/{orderId}/return/request")
//    public ResponseEntity<?> naverProductReturnRequest(@PathVariable(value = "orderId") Long orderId,
//                                                       @RequestParam(value = "returnReason") NaverReturnReasonEnum returnReason) {
//
//        naverProductOrderService.naverReturnRequest(orderId, returnReason);
//        return ApisResponse.toResponseEntity();
//
//    }


}
