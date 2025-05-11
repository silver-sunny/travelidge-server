package com.studio.core.order.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.global.enums.order.payment.PaymentFinancialInstitution;
import com.studio.core.global.enums.order.payment.PaymentSettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "계좌이체로 결제 했을때 이체 정보가 담기는 객체")
public class PaymentTransfer {

    @Schema(description = "은행 두자리 코드")
    private PaymentFinancialInstitution bankCode;

    @Schema(description = "정산 상태")
    private PaymentSettlementStatus settlementStatus;

}
