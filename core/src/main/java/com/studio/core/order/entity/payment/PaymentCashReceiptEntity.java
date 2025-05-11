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
@Table(name = "PAYMENT_CASH_RECEIPT")
@Comment("현금영수증 정보")
public class PaymentCashReceiptEntity {

    @Id
    private Long paymentId;

    @Comment("현금 영수증 종류 | 소득공제, 지출증빙")
    private String type;

    @Comment("현금 영수증의 키값")
    private String receiptKey;

    @Comment("현금영수증 발급 번호")
    private String issueNumber;

    @Comment("발급된 현금영수증을 확인할수 있는 주소")
    private String receiptUrl;

    @Comment("현금영수증 처리된 금액")
    private Long amount;

    @Comment("면세 처리된 금액")
    private Long taxFreeAmount;


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // paymentId를 PaymentEntity의 id로 매핑
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PaymentEntity payment;
}
