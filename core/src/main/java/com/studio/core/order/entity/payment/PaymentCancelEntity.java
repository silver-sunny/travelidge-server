package com.studio.core.order.entity.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Builder
@Entity
@Table(name = "PAYMENT_CANCEL")
@AllArgsConstructor
@NoArgsConstructor
@Comment("결제 취소 정보")
public class PaymentCancelEntity {


    @Id
    @Comment("취소 건의 키값")
    private String transactionKey;

    @Comment("결제를 취소한 금액")
    private Long cancelAmount;

    @Comment("결제를 취소한 이유")
    private String cancelReason;

    @Comment("취소된 금액 중 면세 금액")
    private Long taxFreeAmount;

    @Comment("취소된 금액 중 과세 제외 금액")
    private Long taxExemptionAmount;

    @Comment("결제 취소 후 환불 가능한 잔액")
    private Long refundableAmount;

    @Comment("퀵계좌이체 서비스의 즉시할인에서 취소된 금액")
    private Long transferDiscountAmount;

    @Comment("간편결제 서비스의 포인트, 쿠폰, 즉시할인과 같은 적립식 결제수단에서 취소된 금액")
    private Long easyPayDiscountAmount;

    @Comment("결제 취소가 일어난 날짜와 시간")
    private LocalDateTime canceledAt;

    @Comment("취소 건의 현금 영수증 키값")
    private String receiptKey;

    @Comment("취소 상태")
    private String cancelStatus;

    @Comment("취소 요청 ID")
    private String cancelRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment; // PaymentEntity와의 관계 설정


}
