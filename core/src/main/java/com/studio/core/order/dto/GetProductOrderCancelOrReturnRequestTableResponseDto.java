package com.studio.core.order.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketState;
import com.studio.core.global.response.ChannelDetailDto;
import com.studio.core.global.utils.PhoneNumberUtil;
import com.studio.core.global.utils.TimeCalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;


@Schema(description = "취소/반품 요청 주문 리스트")
public record GetProductOrderCancelOrReturnRequestTableResponseDto(

    @Schema(description = "주문 번호")
    Long id,

    @Schema(description = "구매 채널")
    Channels channel,

    @Schema(description = "소셜 주문 번호")
    String channelOrderId,

    @Schema(description = "소셜 상품 주문 번호")
    String channelProductOrderId,

    @Schema(description = "주문 상태")
    ProductOrderState orderState,

    @Schema(description = "티켓 상태")
    TicketState ticketState,

    @Schema(description = "주문 일시")
    LocalDateTime purchaseAt,


    @Schema(description = "상품 번호")
    Long productId,

    @Schema(description = "상품명")
    String productName,

    @Schema(description = "옵션 (예약 날짜)")
    String directOption,

    @Schema(description = "수량")
    int purchaseQuantity,

    @Schema(description = "구매자 명")
    String purchaseUserName,

    @Schema(description = "구매자 연락처")
    String phoneNumber,

    @Schema(description = "구매자 Id")
    String purchaseUserId,

    @Schema(description = "채널 주문 Id")
    String channelProductId,


    @Schema(description = "취소 요청일")
    LocalDateTime cancelRequestAt,

    @Schema(description = "반품 요청일")
    LocalDateTime returnRequestAt,

    @Schema(description = "반품 or 취소 상태")
    CancelOrReturnState cancelOrReturnState,

    @Schema(description = "취소/반품 사유")
    String cancelOrReturnReason,

    @Schema(description = "취소/반품 상세 사유")
    String cancelOrReturnDetailReason,

    @Schema(description = "통신 대기 여부")
    boolean isProgressing
    ) {

    @QueryProjection
    public GetProductOrderCancelOrReturnRequestTableResponseDto {}

    public String getPurchaseAt() {
        return TimeCalculatorUtil.getFormattedDateTime(purchaseAt);
    }

    public String getOrderStateMeaning() {
        return orderState.getMeaning();
    }

    public String getTicketStateMeaning() {
        return ticketState.getMeaning();
    }

    public String getCancelOrReturnStateMeaning() {
        return cancelOrReturnState.getMeaning();
    }

    public String getCancelOrReturnAt() {
        if (cancelOrReturnState.equals(CancelOrReturnState.CANCEL_REQUEST)) {
            return TimeCalculatorUtil.getFormattedDateTime(cancelRequestAt);

        } else if (cancelOrReturnState.equals(CancelOrReturnState.RETURN_REQUEST)) {
            return TimeCalculatorUtil.getFormattedDateTime(returnRequestAt);

        }
        return "";
    }

    public ChannelDetailDto getChannelDetail() {
        return Channels.getChannelDetail(this.channel, this.productId, this.channelProductId);

    }

    public String getPhoneNumber() {
        return PhoneNumberUtil.formatPhoneNumber(phoneNumber);

    }

}

