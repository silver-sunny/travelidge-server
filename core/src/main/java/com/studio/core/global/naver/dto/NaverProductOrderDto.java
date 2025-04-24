package com.studio.core.global.naver.dto;


import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.NaverClaimType;
import com.studio.core.global.enums.order.ProductOrderState;
import lombok.Getter;

@Getter
public class NaverProductOrderDto {

    // 구매 수량
    private int quantity;

    // 상품 주문번호
    private String productOrderId;

    // 직접입력 예약 옵션
    private String productOption;

    // 주문 번호
    private String productId;

    // 주문 상태
    private ProductOrderState productOrderStatus;

    // 클레임 타입
    private NaverClaimType claimType;

    // 클래입 상태
    private CancelOrReturnState claimStatus;

    // 총 지불 금액
    private long totalPaymentAmount;

    // 발송 기한
    private String shippingDueDate;

}
