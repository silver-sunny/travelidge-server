package com.studio.core.order.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.global.enums.order.payment.PaymentSettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "휴대폰 경제 관련 정보")
public class PaymentMobilePhone {
    
    @Schema(description = "구매자가 결제에 사용한 휴대폰 번호 | -없이 숫자로 구성됨")
    private String customerMobilePhone;


    @Schema(description = "정산 상태")
    private PaymentSettlementStatus settlementStatus ;

    @Schema(description = "휴대폰 결제 내역 영수증 주소")
    private String receiptUrl;


}
