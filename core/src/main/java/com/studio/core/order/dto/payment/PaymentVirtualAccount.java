package com.studio.core.order.dto.payment;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.global.enums.order.payment.PaymentFinancialInstitution;
import com.studio.core.global.enums.order.payment.PaymentRefundStatus;
import com.studio.core.global.enums.order.payment.PaymentSettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "가상계좌로 결제하면 제공되는 가상계좌 관련 정보")
public class PaymentVirtualAccount {

    @Id
    private Long paymentId;

    @Schema(description = "가상계좌 타입 | 일반, 고정")
    private String accountType;

    @Schema(description = "발급된 계좌정보")
    private String accountNumber;

    @Schema(description = "가상계좌를 발급한 구매자명입니다")
    private String customerName;

    @Schema(description = "입급 기한")
    private PaymentFinancialInstitution bankCode;

    @Schema(description = "환불 처리 상태입니다")
    private PaymentRefundStatus refundStatus;

    @Schema(description = "가상계좌의 만료 여부")
    private boolean expired;

    @Schema(description = "정산상태")
    private PaymentSettlementStatus settlementStatus;

    @Schema(description = "결제위젯 가상계좌 환불 정보 입력 기능으로 받은 구매자의 환불 계좌 정보입니다.")
    private String refundReceiveAccount;

}
