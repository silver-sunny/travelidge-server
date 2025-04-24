package com.studio.core.order.entity.payment;



import com.studio.core.global.enums.converter.PaymentFinancialInstitutionConverter;
import com.studio.core.global.enums.order.payment.PaymentFinancialInstitution;
import com.studio.core.global.enums.order.payment.PaymentRefundStatus;
import com.studio.core.global.enums.order.payment.PaymentSettlementStatus;
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
@Table(name = "PAYMENT_VIRTUAL_ACCOUNT")
@Comment("가상계좌로 결제하면 제공되는 가상계좌 관련 정보")
public class PaymentVirtualAccountEntity {

    @Id
    private Long paymentId;

    @Comment("가상계좌 타입 | 일반, 고정")
    private String accountType;

    @Comment("발급된 계좌정보")
    private String accountNumber;

    @Comment("가상계좌를 발급한 구매자명입니다")
    private String customerName;

    @Comment("입급 기한")
    @Convert(converter = PaymentFinancialInstitutionConverter.class)
    private PaymentFinancialInstitution bankCode;

    @Comment("환불 처리 상태입니다")
    private PaymentRefundStatus refundStatus;

    @Comment("가상계좌의 만료 여부")
    private boolean expired;

    @Comment("정산상태")
    private PaymentSettlementStatus settlementStatus;

    @Comment("결제위젯 가상계좌 환불 정보 입력 기능으로 받은 구매자의 환불 계좌 정보입니다.")
    private String refundReceiveAccount;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // paymentId를 PaymentEntity의 id로 매핑
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PaymentEntity payment;
}
