package com.studio.core.order.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.global.enums.order.PaymentStatus;
import com.studio.core.global.enums.order.payment.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record GetPaymentSuccessResponseDto(


    @Schema(description = "결제 키값")
    String paymentKey,

    @Schema(description = "주문 키값")
    String orderId,

    @Schema(description = "총 결제 금액")
    Long totalAmount,

    @Schema(description = "결제 처리 상태")
    PaymentStatus status,

    @Schema(description = "결제가 일어난 날짜와 시간")
    OffsetDateTime requestedAt,

    @Schema(description = "결제 승인이 일어난 날짜와 시간")
    OffsetDateTime approvedAt,

    @Schema(description = "결제 타입 정보")
    PaymentType type,

    @Schema(description = "구매상품")
    String orderName,

    @Schema(description = "상점아이디(MID)")
    String mId,

    @Schema(description = "결제 통화")
    String currency,

    @Schema(description = "결제수단")
    String method,

    @Schema(description = "취소할 수 있는 금액(잔고)")
    Long balanceAmount,

    @Schema(description = "에스크로 사용 여부")
    boolean useEscrow,

    @Schema(description = "마지막 거래의 키값")
    String lastTransactionKey,

    @Schema(description = "공급가액")
    Long suppliedAmount,

    @Schema(description = "부가세")
    Long vat,

    @Schema(description = "문화비(도서, 공연 티켓, 박물관·미술관 입장권 등) 지출 여부")
    boolean cultureExpense,

    @Schema(description = "결제 금액 중 면세 금액")
    Long taxFreeAmount,

    @Schema(description = "과세를 제외한 결제 금액")
    Long taxExemptionAmount,

    @Schema(description = "결제 취소 이력")
    List<PaymentCancel> cancels,

    @Schema(description = " 부분 취소 가능 여부입니다. 이 값이 false이면 전액 취소만 가능합니다.")
    boolean isPartialCancelable,

    @Schema(description = "카드 결제 정보")
    PaymentCard card,

    @Schema(description = "가상계좌 결제 정보")
    PaymentVirtualAccount virtualAccount,

    @Schema(description = "웹훅을 검증하는 값")
    String secret,

    @Schema(description = "휴대폰 결제 정보")
    PaymentMobilePhone mobilePhone,

    @Schema(description = "상품권 결제 정보")
    PaymentGiftCertificate giftCertificate,

    @Schema(description = "계좌이체 결제 정보")
    PaymentTransfer transfer,

    @Schema(description = "결제 요청 시 SDK에서 직접 추가할 수 있는 결제 관련 정보")
    String metadata,

    @Schema(description = "결제수단별 영수증")
    PaymentUrl receipt,

    @Schema(description = "결제창 주소")
    PaymentUrl checkout,


    @Schema(description = "간편결제 정보")
    PaymentEasyPay easyPay,

    @Schema(description = "결제 승인에 실패하면 응답으로 받는 에러 객체")
    PaymentFailure failure,

    @Schema(description = "현금영수증 정보")
    PaymentCashReceipt cashReceipt
) {
}
