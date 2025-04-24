package com.studio.core.order.dto.payment;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "결제 승인에 실패하면 응답받는 에러")
public class PaymentFailure {

    @Schema(description = "오류타입")
    private String code;

    @Schema(description = "에러 메세지")
    private String message;
}
