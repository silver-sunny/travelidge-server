package com.studio.core.order.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "결제 취소 정보")
public class PaymentCancel {


    @Schema(description = "취소 건의 키값")
    private String transactionKey;

    @Schema(description = "결제를 취소한 금액")
    private Long cancelAmount;

    @Schema(description = "결제를 취소한 이유")
    private String cancelReason;

    @Schema(description = "취소된 금액 중 면세 금액")
    private Long taxFreeAmount;

    @Schema(description = "취소된 금액 중 과세 제외 금액")
    private Long taxExemptionAmount;

    @Schema(description = "결제 취소 후 환불 가능한 잔액")
    private Long refundableAmount;

    @Schema(description = "퀵계좌이체 서비스의 즉시할인에서 취소된 금액")
    private Long transferDiscountAmount;

    @Schema(description = "간편결제 서비스의 포인트, 쿠폰, 즉시할인과 같은 적립식 결제수단에서 취소된 금액")
    private Long easyPayDiscountAmount;

    @Schema(description = "결제 취소가 일어난 날짜와 시간")
    private LocalDateTime canceledAt;

    @Schema(description = "취소 건의 현금 영수증 키값")
    private String receiptKey;

    @Schema(description = "취소 상태")
    private String cancelStatus;

    @Schema(description = "취소 요청 ID")
    private String cancelRequestId;


}
