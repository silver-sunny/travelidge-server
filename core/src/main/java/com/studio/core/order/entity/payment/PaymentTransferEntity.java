package com.studio.core.order.entity.payment;

import com.studio.core.global.enums.order.payment.PaymentFinancialInstitution;
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
@Table(name = "PAYMENT_TRANSFER")
@Comment("계좌이체로 결제 했을때 이체 정보가 담기는 객체")
public class PaymentTransferEntity {

    @Id
    private Long paymentId;

    @Comment("은행 두자리 코드")
    private PaymentFinancialInstitution bankCode;

    @Comment("정산 상태")
    private PaymentSettlementStatus settlementStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // paymentId를 PaymentEntity의 id로 매핑
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PaymentEntity payment;
}
