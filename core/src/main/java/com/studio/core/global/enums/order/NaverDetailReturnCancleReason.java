package com.studio.core.global.enums.order;

import lombok.Getter;

@Getter
public enum NaverDetailReturnCancleReason {

    ETC("없음"),
    SIMPLE_INTENT_CHANGED("단순 변심"),
    MISTAKE_ORDER("주문실수"),
    DELAYED_DELIVERY_BY_PURCHASER("배송 지연"),
    PRODUCT_UNSATISFIED_BY_PURCHASER("서비스 불만"),
    INTENT_CHANGED("구매의사취소"),
    COLOR_AND_SIZE("색상 및 사이즈 변경"),
    WRONG_ORDER("다른 상품 잘못 주문"),
    PRODUCT_UNSATISFIED("서비스 불만족"),
    DELAYED_DELIVERY("배송지연"),
    SOLD_OUT("상품품절"),
    DROPPED_DELIVERY("배송누락"),
    BROKEN("상품파손"),
    INCORRECT_INFO("상품정보 상이"),
    WRONG_DELIVERY("오배송"),
    WRONG_OPTION("색상 등 다른상품 잘못 배송"),
    BROKEN_AND_BAD("파손 및 불량"),
    WRONG_DELAYED_DELIVERY("오배송 및 지연");

    private final String meaning;

    NaverDetailReturnCancleReason(String meaning) {
        this.meaning = meaning;
    }
}
