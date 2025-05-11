package com.studio.core.order.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.utils.PhoneNumberUtil;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 정보 요약")
public record GetProductOrderSummaryDto(

    @Schema(description = "주문 번호")
    Long id,

    @Schema(description = "상품명")
    String productName,

    @Schema(description = "구매자 명")
    String purchaseUserName,

    @Schema(description = "구매자 연락처")
    String phoneNumber,

    @Schema(description = "옵션 (예약 날짜)")
    String directOption
) {
    @QueryProjection
    public GetProductOrderSummaryDto {}


    public String getPhoneNumber() {
        return PhoneNumberUtil.formatPhoneNumber(phoneNumber);

    }
}
