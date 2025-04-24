package com.studio.core.global.naver.dto;


import com.studio.core.global.enums.order.ProductOrderState;
import lombok.Getter;

@Getter
public class NaverLastChangeStatusDto {

    //언제 마지막으로 바꼈는지
    private String orderId;

    // 주문번호
    private String productOrderId;

    // 주문 상태
    private ProductOrderState productOrderStatus;

    // 주문 날짜
    private String claimType;

    private String paymentDate;


    private String claimStatus;

    private String lastChangedDate;

    private boolean receiverAddressChanged;

    private String lastChangedType;


}