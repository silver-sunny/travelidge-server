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
@Table(name = "PAYMENT_MOBILE_PHONE")
@Comment("휴대폰 경제 관련 정보")
public class PaymentMobilePhoneEntity {

    @Id
    private Long paymentId;

    @Comment("구매자가 결제에 사용한 휴대폰 번호 | -없이 숫자로 구성됨")
    private String customerMobilePhone;


    @Comment("정산 상태")
    private PaymentSettlementStatus settlementStatus ;

    @Comment("휴대폰 결제 내역 영수증 주소")
    private String receiptUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // paymentId를 PaymentEntity의 id로 매핑
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PaymentEntity payment;
}
