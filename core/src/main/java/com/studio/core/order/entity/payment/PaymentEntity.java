package com.studio.core.order.entity.payment;

import com.studio.core.global.enums.order.PaymentStatus;
import com.studio.core.global.enums.order.payment.PaymentType;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.dto.payment.GetPaymentSuccessResponseDto;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.product.entity.ProductEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@Entity
@Table(name = "PAYMENT")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Getter
public class PaymentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("결제 키값")
    private String paymentKey;

    @Comment("주문키값")
    private String channelProductOrderId;

    @Comment("가격")
    private Long productPrice;

    @Comment("할인율")
    private Long productDiscountRate;

    @Comment("총 결제 금액")
    private Long totalAmount;

    @Comment("구매수량")
    private int purchaseQuantity;

    @Comment("핸드폰 번호")
    private String phoneNumber;

    @Comment("구매자 이름")
    private String purchaseUserName;

    @Comment("옵션 직접입력 ( 예약날짜 )")
    private String directOption;

    @Enumerated(EnumType.STRING)
    @Comment("결제 처리 상태")
    private PaymentStatus status;

    @Comment("결제가 일어난 날짜와 시간")
    private LocalDateTime requestedAt;

    @Comment("결제 승인이 일어난 날짜와 시간")
    private LocalDateTime approvedAt;

    @Comment("결제 타입 정보")
    private PaymentType type;

    @Comment("구매상품")
    private String orderName;

    @Comment("상점아이디(MID)")
    private String mId;

    @Comment("결제 통화")
    private String currency;

    @Comment("결제수단")
    private String method;

    @Comment("취소할 수 있는 금액(잔고)")
    private Long balanceAmount;

    @Comment("에스크로 사용 여부")
    private boolean useEscrow;

    @Comment("마지막 거래의 키값")
    private String lastTransactionKey;

    @Comment("공급가액")
    private Long suppliedAmount;

    @Comment("부가세")
    private Long vat;

    @Comment("문화비(도서, 공연 티켓, 박물관·미술관 입장권 등) 지출 여부")
    private boolean cultureExpense;

    @Comment("결제 금액 중 면세 금액")
    private Long taxFreeAmount;

    @Comment("과세를 제외한 결제 금액")
    private Long taxExemptionAmount;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("결제 취소 이력")
    private List<PaymentCancelEntity> cancels;

    @Comment(" 부분 취소 가능 여부입니다. 이 값이 false이면 전액 취소만 가능합니다.")
    private boolean isPartialCancelable;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("카드 결제 정보")
    private PaymentCardEntity card;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("가상계좌 결제 정보")
    private PaymentVirtualAccountEntity virtualAccount;

    @Comment("웹훅을 검증하는 값")
    private String secret;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("휴대폰 결제 정보")
    private PaymentMobilePhoneEntity mobilePhone;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("상품권 결제 정보")
    private PaymentGiftCertificateEntity giftCertificate;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("계좌이체 결제 정보")
    private PaymentTransferEntity transfer;

    @Comment("결제 요청 시 SDK에서 직접 추가할 수 있는 결제 관련 정보")
    private String metadata;

    @Comment("결제수단별 영수증")
    private String receiptUrl;

    @Comment("결제창 주소")
    private String checkoutUrl;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("간편결제 정보")
    private PaymentEasyPayEntity easyPay;


    @Comment("실패 에러코드")
    private String failureCode;

    @Comment("실패 에러 메세지")
    private String failureMessage;


    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("현금영수증 정보")
    private PaymentCashReceiptEntity cashReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberAuthEntity member;



    @PrePersist
    public void generateUUID() {
        if (channelProductOrderId == null) {
            channelProductOrderId = UUID.randomUUID().toString();
        }
    }
    public void updatePayment(GetPaymentSuccessResponseDto responseDto){

        this.status = responseDto.status();
        this.requestedAt = responseDto.requestedAt() != null ? responseDto.requestedAt().toLocalDateTime() : null;
        this.approvedAt = responseDto.approvedAt() != null ? responseDto.approvedAt().toLocalDateTime() : null;
        this.paymentKey = responseDto.paymentKey();
        this.totalAmount = responseDto.totalAmount();
        this.orderName = responseDto.orderName();
        this.mId = responseDto.mId();
        this.currency = responseDto.currency();
        this.method = responseDto.method();
        this.balanceAmount = responseDto.balanceAmount();
        this.useEscrow = responseDto.useEscrow();
        this.lastTransactionKey = responseDto.lastTransactionKey();
        this.suppliedAmount = responseDto.suppliedAmount();
        this.vat = responseDto.vat();
        this.cultureExpense = responseDto.cultureExpense();
        this.taxFreeAmount = responseDto.taxFreeAmount();
        this.taxExemptionAmount = responseDto.taxExemptionAmount();
//        this.cancels = responseDto.cancels();

        this.isPartialCancelable = responseDto.isPartialCancelable();
//        this.card = responseDto.card();
//        this.virtualAccount = responseDto.virtualAccount();
        this.secret = responseDto.secret();
//        this.mobilePhone = responseDto.mobilePhone();
//        this.giftCertificate = responseDto.giftCertificate();
//        this.transfer = responseDto.transfer();
        this.metadata = responseDto.metadata();
        this.receiptUrl = responseDto.receipt().getUrl();  // Assuming receipt is a nested object that has the getUrl() method
        this.checkoutUrl = responseDto.checkout().getUrl();  // Assuming checkout is a nested object that has the getUrl() method
//        this.easyPay = responseDto.easyPay();
        this.failureCode = responseDto.failure() != null ? responseDto.failure().getCode() : null;  // Assuming failure is an object with errorCode
        this.failureMessage = responseDto.failure() != null ? responseDto.failure().getMessage() : null;  // Assuming failure has a message field
//        this.cashReceipt = responseDto.cashReceipt();
        this.type = responseDto.type();
    }

    public void updateOrder(OrderEntity order){
        this.order = order;
    }

}
