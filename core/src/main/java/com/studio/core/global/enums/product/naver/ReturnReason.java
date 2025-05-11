package com.studio.core.global.enums.product.naver;

import com.studio.core.global.repository.Display;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum ReturnReason {

    INTENT_CHANGED("구매 의사 취소"),
    COLOR_AND_SIZE("색상 및 사이즈 변경"),
    WRONG_ORDER("다른 상품 잘못 주문"),
    PRODUCT_UNSATISFIED("서비스 불만족"),
    INCORRECT_INFO("상품 정보 상이"),
    WRONG_OPTION("색상 등 다른 상품 잘못 배송");

    private final String meaning;

    ReturnReason(String meaning) {
        this.meaning = meaning;
    }

    public static List<Display> getReturnReasons() {
        return Arrays.stream(ReturnReason.values())
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }

}
