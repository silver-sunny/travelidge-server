package com.studio.core.order.dto.payment;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.global.enums.order.payment.PaymentSettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "상품권 결제 관련 정보")
public class PaymentGiftCertificate {


    @Schema(description = "결제 승인 번호")
    private String approveNo;

    @Schema(description = "정산 상태")
    private PaymentSettlementStatus settlementStatus;


}
