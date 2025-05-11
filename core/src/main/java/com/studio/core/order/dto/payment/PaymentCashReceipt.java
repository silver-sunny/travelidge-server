package com.studio.core.order.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "현금영수증 정보")
public class PaymentCashReceipt {


    @Schema(description = "현금 영수증 종류 | 소득공제, 지출증빙")
    private String type;

    @Schema(description = "현금 영수증의 키값")
    private String receiptKey;

    @Schema(description = "현금영수증 발급 번호")
    private String issueNumber;

    @Schema(description = "발급된 현금영수증을 확인할수 있는 주소")
    private String receiptUrl;

    @Schema(description = "현금영수증 처리된 금액")
    private Long amount;

    @Schema(description = "면세 처리된 금액")
    private Long taxFreeAmount;

}
