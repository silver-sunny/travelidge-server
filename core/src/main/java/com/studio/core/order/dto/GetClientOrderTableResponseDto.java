package com.studio.core.order.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketState;
import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.global.utils.TimeCalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;


@Schema(description = "구매내역 리스트")
public record GetClientOrderTableResponseDto(

    @Schema(description = "주문 번호")
    Long id,

    @Schema(description = "채널상품주문 번호(예매번호)")
    String channelProductOrderId,

    @Schema(description = "주문 상태")
    ProductOrderState orderState,

    @Schema(description = "반품 or 취소 상태")
    CancelOrReturnState cancelOrReturnState,

    @Schema(description = "티켓 상태")
    TicketState ticketState,

    @Schema(description = "주문 일시")
    LocalDateTime purchaseAt,

    @Schema(description = "상품 번호")
    Long productId,

    @Schema(description = "상품명")
    String productName,

    @Schema(description = "대표이미지")
    String pri,

    @Schema(description = "옵션 (예약 날짜)")
    String directOption,

    @Schema(description = "티켓 사용여부")
    TicketUsedState ticketUsedState
    ) {

    @QueryProjection
    public GetClientOrderTableResponseDto {
    }

    public String getPurchaseAt() {
        return TimeCalculatorUtil.getFormattedDateTime(purchaseAt);
    }

    public String getOrderStateMeaning() {
        return orderState.getMeaning();
    }

    public String getTicketStateMeaning() {
        return ticketState.getMeaning();
    }


    public String getTicketUsedStateMeaning() {
        return ticketUsedState.getMeaning();
    }


    public String getCancelOrReturnStateMeaning() {
        return cancelOrReturnState.getMeaning();

    }

    @Schema(description = "취소/반품 어떤상태가 가능한지")
    public CancelOrReturnState getAvailableCancelOrReturnState() {

        if (ProductOrderState.PAYED.equals(orderState)) {
            // 결제 완료-> 취소요청 가능
            return CancelOrReturnState.CANCEL_REQUEST;
        } else if (ProductOrderState.DELIVERED.equals(orderState)) {
            // 티켓 발급했을경우
            if (!TicketUsedState.USED.equals(ticketUsedState)) {
                // 티켓 사용 가능
                if (CancelOrReturnState.NOTHING.equals(cancelOrReturnState)) {
                    return CancelOrReturnState.RETURN_REQUEST;
                } else if (CancelOrReturnState.RETURN_REQUEST.equals(cancelOrReturnState)) {
                    // 반품 요청일경우
                    return CancelOrReturnState.RETURN_REJECT;
                } else if (CancelOrReturnState.RETURN_REJECT.equals(cancelOrReturnState)) {
                    return CancelOrReturnState.RETURN_REQUEST;

                }
            }
        }
        return CancelOrReturnState.NOTHING;
    }

    @Schema(description = "취소/반품 가능 상태의 의미")
    public String getAvailableCancelOrReturnStateMeaning() {
        CancelOrReturnState availableState = getAvailableCancelOrReturnState();
        return availableState.getMeaning();
    }
}

