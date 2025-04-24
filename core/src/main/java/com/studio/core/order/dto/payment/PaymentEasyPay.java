package com.studio.core.order.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "간편결제 정보")
public class PaymentEasyPay {

    @Schema(description = "간편결제사 코드입니다.")
    private String provider;

    @Schema(description = "간편결제 서비스에 등록된 계좌 혹은 현금성 포인트로 결제한 금액입니다")
    private Long amount;

    @Schema(description = "간편결제 서비스의 적립 포인트나 쿠폰 등으로 즉시 할인된 금액입니다.")
    private Long discountAmount;

}
