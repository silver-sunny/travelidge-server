package com.studio.core.ticket.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.global.response.ChannelDetailDto;
import com.studio.core.global.utils.PhoneNumberUtil;
import com.studio.core.global.utils.TimeCalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Schema(description = "티켓 사용처리 리스트")
public record GetTicketTableResponseDto(

    //티켓
    @Schema(description = "티켓 키")
    String ticketKey,

    @Schema(description = "사용여부")
    TicketUsedState ticketUsedState,

    @Schema(description = "사용일")
    LocalDateTime usedAt,

    // 주문
    @Schema(description = "상품 주문 번호")
    Long productOrderId,

    @Schema(description = "구매 채널")
    Channels channel,

    @Schema(description = "소셜 주문 아이디")
    String channelOrderId,

    @Schema(description = "소셜 상품 주문 아이디")
    String channelProductOrderId,

    @Schema(description = "구매자 명")
    String purchaseUserName,

    @Schema(description = "구매자 연락처")
    String phoneNumber,

    @Schema(description = "주문 상태")
    ProductOrderState orderState,

    @Schema(description = "취소/반품 상태")
    CancelOrReturnState cancelOrReturnState,

    @Schema(description = "주문 일시")
    LocalDateTime purchaseAt,

    @Schema(description = "수량")
    int purchaseQuantity,

    @Schema(description = "옵션 (예약 날짜)")
    String directOption,

    //상품
    @Schema(description = "상품 번호")
    Long productId,

    @Schema(description = "상품명")
    String productName,


    @Schema(description = "채널 주문 Id")
    String channelProductId
) {

    @QueryProjection
    public GetTicketTableResponseDto {
    }

    public String getOrderStateMeaning() {
        return orderState.getMeaning();
    }

    public String getUsedAt() {
        return TimeCalculatorUtil.getFormattedDateTime(usedAt);
    }

    public String getCancelOrReturnStateMeaning() {
        return cancelOrReturnState == null ? CancelOrReturnState.NOTHING.getMeaning()
            : cancelOrReturnState.getMeaning();
    }

    @Schema(description = "티켓 사용가능여부")
    public String getTicketUsedStateMeaning() {
        return ticketUsedState.getMeaning();
    }

    // 상품 상세정보
    public ChannelDetailDto getChannelDetail() {
        return Channels.getChannelDetail(this.channel, this.productId, this.channelProductId);

    }

    public String getPhoneNumber() {
        return PhoneNumberUtil.formatPhoneNumber(phoneNumber);

    }

    public String getPurchaseAt() {
        return TimeCalculatorUtil.getFormattedDateTime(purchaseAt);
    }

}

