package com.studio.core.order.entity.payment;

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
@Table(name = "PAYMENT_EASYPAY")
@Comment("간편결제 정보")
public class PaymentEasyPayEntity {

    @Id
    private Long paymentId;

    @Comment("간편결제사 코드입니다.")
    private String provider;

    @Comment("간편결제 서비스에 등록된 계좌 혹은 현금성 포인트로 결제한 금액입니다")
    private Long amount;

    @Comment("간편결제 서비스의 적립 포인트나 쿠폰 등으로 즉시 할인된 금액입니다.")
    private Long discountAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // paymentId를 PaymentEntity의 id로 매핑
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PaymentEntity payment;
}
