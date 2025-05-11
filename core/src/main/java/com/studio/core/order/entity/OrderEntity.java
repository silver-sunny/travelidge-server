package com.studio.core.order.entity;


import static com.studio.core.global.utils.DateUtil.stringDateTimeToLocalDateTimeWithFormat;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.converter.CancelOrReturnStateConverter;
import com.studio.core.global.enums.converter.ChannelsConverter;
import com.studio.core.global.enums.converter.ProductOrderStateConverter;
import com.studio.core.global.enums.converter.TicketStateConverter;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketState;
import com.studio.core.global.enums.product.naver.CancelReason;
import com.studio.core.global.enums.product.naver.ReturnReason;
import com.studio.core.global.naver.dto.NaverCancelDto;
import com.studio.core.global.naver.dto.NaverDataDto;
import com.studio.core.global.naver.dto.NaverOrderInfoDto;
import com.studio.core.global.naver.dto.NaverProductOrderDto;
import com.studio.core.global.naver.dto.NaverReturnDto;
import com.studio.core.global.repository.TimeBaseEntity;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.ticket.entity.TicketEntity;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(
    name = "PRODUCT_ORDER",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_channel_order", columnNames = {"channelOrderId",
            "channelProductOrderId"}),
        @UniqueConstraint(name = "UK_channel_order", columnNames = {"channel",
            "channelProductOrderId"}),

    }
)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Getter
public class OrderEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("주문 번호")
    private Long id;

    @Comment("구매채널")
    @Convert(converter = ChannelsConverter.class)
    private Channels channel;

    @Comment("소셜주문번호")
    private String channelOrderId;

    @Comment("주문상태")
    @Convert(converter = ProductOrderStateConverter.class)
    private ProductOrderState orderState;

    @Comment("반품 or 취소 상태 번호")
    @Convert(converter = CancelOrReturnStateConverter.class)
    private CancelOrReturnState cancelOrReturnState;

    @Comment("구매일")
    private LocalDateTime purchaseAt;

    @Comment("구매금액")
    private long purchasePrice;

    @Comment("구매수량")
    private int purchaseQuantity;

    @Comment("구매자 아이디")
    private String purchaseUserId;

    @Comment("구매자 이름")
    private String purchaseUserName;

    @Comment("구매자 연락처")
    private String phoneNumber;

    @Comment("취소 요청일")
    private LocalDateTime cancelRequestAt;

    @Comment("취소 철회일")
    private LocalDateTime cancelRejectAt;

    @Comment("취소 완료일")
    private LocalDateTime cancelDoneAt;

    @Comment("반품 요청일")
    private LocalDateTime returnRequestAt;

    @Comment("반품 철회일")
    private LocalDateTime returnRejectAt;

    @Comment("반품 완료일")
    private LocalDateTime returnDoneAt;

    @Comment("옵션(소셜상품)주문번호")
    private String channelProductOrderId;

    @Comment("취소/반품 사유")
    private String cancelOrReturnReason;

    @Comment("취소/반품 상세 사유")
    private String cancelOrReturnDetailReason;

    @Comment("취소/반품 요청자")
    private String cancelOrReturnRequester;

    @Comment("통신 대기 여부")
    @ColumnDefault("0")
    @Column(nullable = false)
    private boolean isProgressing;

    @Comment("옵션 직접입력 ( 예약날짜 )")
    private String directOption;

    @Comment("티켓 상태 번호")
    @Convert(converter = TicketStateConverter.class)
    @ColumnDefault("0")
    @Column(nullable = false)
    private TicketState ticketState;

    @Comment("발송 기한")
    private LocalDateTime shippingDueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberAuthEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductEntity product;


    public void updateOrder(NaverDataDto naverData) {

        this.channel = Channels.NAVER;

        NaverProductOrderDto npr = naverData.getProductOrder();
        NaverOrderInfoDto noi = naverData.getOrder();

        this.orderState = npr.getProductOrderStatus();

        this.channelOrderId = noi.getOrderId();
        Optional<String> productOption = Optional.ofNullable(npr.getProductOption());

// 값이 존재하는 경우에만 "예약날짜:"를 제거하고 결과를 this.scheduleAt에 할당
        productOption.ifPresent(option -> this.directOption = option.replace("예약날짜: ", ""));
        this.purchaseQuantity = npr.getQuantity();
        this.purchasePrice = npr.getTotalPaymentAmount();
        this.channelProductOrderId = npr.getProductOrderId();

        this.phoneNumber = noi.getOrdererTel();
        this.purchaseUserId = noi.getOrdererId();

        this.purchaseAt = stringDateTimeToLocalDateTimeWithFormat(noi.getPaymentDate());
        this.shippingDueDate = stringDateTimeToLocalDateTimeWithFormat(npr.getShippingDueDate());

        // 판매자가 취소
        NaverReturnDto returnDto = naverData.getReturnDto();

        // 구매자가 취소
        NaverCancelDto cancelDto = naverData.getCancel();

        if (cancelDto != null) {

            // 취소 ( 환불) 신청 날짜
            this.cancelRequestAt = stringDateTimeToLocalDateTimeWithFormat(
                cancelDto.getClaimRequestDate());

            this.cancelOrReturnReason = cancelDto.getCancelReason().getMeaning();
            this.cancelOrReturnDetailReason = cancelDto.getCancelDetailedReason();
            this.cancelOrReturnRequester = cancelDto.getRequestChannel();

            if (CancelOrReturnState.CANCEL_REJECT.equals(cancelDto.getClaimStatus())) {

                // 취소 철회.거부 한날짜
                this.cancelRejectAt = stringDateTimeToLocalDateTimeWithFormat(
                    cancelDto.getCancelCompletedDate());

            } else if (CancelOrReturnState.CANCEL_DONE.equals(cancelDto.getClaimStatus())) {

                this.cancelDoneAt = stringDateTimeToLocalDateTimeWithFormat(
                    cancelDto.getCancelCompletedDate());
            }

        }

        if (returnDto != null) {
            // 반품  ( 환불 ) 한 날짜
            this.returnRequestAt = stringDateTimeToLocalDateTimeWithFormat(
                returnDto.getClaimRequestDate());

            this.cancelOrReturnReason = returnDto.getReturnReason().getMeaning();
            this.cancelOrReturnDetailReason = returnDto.getReturnDetailedReason();
            this.cancelOrReturnRequester = returnDto.getRequestChannel();

            if (CancelOrReturnState.RETURN_REJECT.equals(returnDto.getClaimStatus())) {

                this.returnRejectAt = stringDateTimeToLocalDateTimeWithFormat(
                    returnDto.getReturnCompletedDate());
            } else if (CancelOrReturnState.RETURN_DONE.equals(returnDto.getClaimStatus())) {

                this.returnDoneAt = stringDateTimeToLocalDateTimeWithFormat(
                    returnDto.getReturnCompletedDate());
            }

        }

        this.cancelOrReturnState = npr.getClaimStatus();
    }

    // 반품 승인
    public void returnApproveOrder() {

        this.returnDoneAt = LocalDateTime.now();
        this.orderState = ProductOrderState.RETURNED;
        this.cancelOrReturnState = CancelOrReturnState.RETURN_DONE;


    }

    // 판매자쪽에서 반품 요청, 승인
    public void returnOrder(ReturnReason returnReason) {

        LocalDateTime returnAt = LocalDateTime.now();
        this.returnRequestAt = returnAt;
        this.returnDoneAt = returnAt;
        this.orderState = ProductOrderState.RETURNED;
        this.cancelOrReturnState = CancelOrReturnState.RETURN_DONE;
        this.cancelOrReturnReason = returnReason.getMeaning();
        this.cancelOrReturnRequester = "판매자";


    }

    // 구매자쪽에서 반품 요정
    public void returnOrderRequest(ReturnReason returnReason) {

        this.returnRequestAt = LocalDateTime.now();
        this.cancelOrReturnState = CancelOrReturnState.RETURN_REQUEST;
        this.cancelOrReturnReason = returnReason.getMeaning();
        this.cancelOrReturnRequester = "구매자";


    }

    public void returnOrderCancel() {

        this.returnRejectAt = LocalDateTime.now();
        this.cancelOrReturnState = CancelOrReturnState.RETURN_REJECT;


    }

    //  취소
    public void cancelOrder(CancelReason cancelReason, String cancelOrReturnRequester){

        LocalDateTime cancelAt = LocalDateTime.now();
        this.cancelRequestAt = cancelAt;
        this.cancelDoneAt = cancelAt;
        this.orderState = ProductOrderState.CANCELED;
        this.cancelOrReturnState = CancelOrReturnState.CANCEL_DONE;
        this.cancelOrReturnReason = cancelReason.getMeaning();
        this.cancelOrReturnRequester = cancelOrReturnRequester;
    }
    // 구매자쪽에서취소 취소

    /**
     * 반품 거부
     * @param cancelOrReturnReason
     */
    public void returnRejectOrder(String cancelOrReturnReason) {
        this.returnRejectAt = LocalDateTime.now();
        this.cancelOrReturnState = CancelOrReturnState.RETURN_REJECT;
        this.cancelOrReturnReason = cancelOrReturnReason;
    }

    public void updateTicketState(TicketState state, ProductOrderState orderState) {
        this.ticketState = state;
        this.orderState = orderState;
    }
}
