package com.studio.core.order.dto.payment;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.global.enums.order.payment.PaymentCardCompany;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "카드로 결제하면 제공되는 카드 관련 정보")
public class PaymentCard {
    

    @Schema(description = "카드사에 결제 요청한 금액")
    private Long amount;

    @Schema(description = "카드번호")
    private String number;

    @Schema(description = "카드 발급사 두 자리 코드")
    private PaymentCardCompany issuerCode;

    @Schema(description = "카드 매입사 두자리 코드")
    private PaymentCardCompany acquirerCode;

    @Schema(description = "할부 개월 수")
    private Integer installmentPlanMonths;

    @Schema(description = "카드사 승인 번호")
    private String approveNo;

    @Schema(description = "카드사 포인트 사용 여부")
    private Boolean useCardPoint;

    @Schema(description = "카드사 포인트 사용 여부")
    private String cardType;

    @Schema(description = "카드의 소유자 타입 | 개인,법인,미확인")
    private String ownerType;

}
