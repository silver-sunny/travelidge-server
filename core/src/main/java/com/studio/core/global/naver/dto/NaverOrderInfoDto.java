package com.studio.core.global.naver.dto;

import lombok.Getter;

@Getter
public class NaverOrderInfoDto {

    // 주문자 아이디
    private String ordererId;

    // 주문자 이름
    private String ordererName;

    // 주문번호
    private String orderId;

    // 주문자 번호
    private String ordererTel;

    // 결제 날짜
    private String paymentDate;


}
