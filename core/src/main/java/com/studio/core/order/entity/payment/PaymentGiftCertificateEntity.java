package com.studio.core.order.entity.payment;


import com.studio.core.global.enums.order.payment.PaymentSettlementStatus;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Comment;


@Entity
@Table(name = "PAYMENT_GIFT_CERTIFICATE")
@Comment("상품권 결제 관련 정보")
public class PaymentGiftCertificateEntity {

    @Id
    private Long paymentId;

    @Comment("결제 승인 번호")
    private String approveNo;

    @Comment("정산 상태")
    private PaymentSettlementStatus settlementStatus;


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // paymentId를 PaymentEntity의 id로 매핑
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PaymentEntity payment;
}
