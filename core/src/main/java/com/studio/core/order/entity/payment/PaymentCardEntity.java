package com.studio.core.order.entity.payment;


import com.studio.core.global.enums.converter.PaymentCartCompanyConverter;
import com.studio.core.global.enums.order.payment.PaymentCardCompany;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
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
@Table(name = "PAYMENT_CARD")
@Comment("카드로 결제하면 제공되는 카드 관련 정보")
public class PaymentCardEntity {

    @Id
    @Comment("결제 번호")
    private Long paymentId;

    @Comment("카드사에 결제 요청한 금액")
    private Long amount;

    @Comment("카드번호")
    private String cardNumber;

    @Convert(converter = PaymentCartCompanyConverter.class)
    @Comment("카드 발급사 두 자리 코드")
    private PaymentCardCompany issuerCode;

    @Convert(converter = PaymentCartCompanyConverter.class)
    @Comment("카드 매입사 두자리 코드")
    private PaymentCardCompany acquirerCode;

    @Comment("할부 개월 수")
    private Integer installmentPlanMonths;

    @Comment("카드사 승인 번호")
    private String approveNo;

    @Comment("카드사 포인트 사용 여부")
    private Boolean useCardPoint;

    @Comment("카드사 포인트 사용 여부")
    private String cardType;

    @Comment("카드의 소유자 타입 | 개인,법인,미확인")
    private String ownerType;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // paymentId를 PaymentEntity의 id로 매핑
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PaymentEntity payment;

}
