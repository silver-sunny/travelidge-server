package com.studio.core.global.enums.order.search;

import com.studio.core.global.repository.Display;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum SearchCondition {
    PHONE_NUMBER("구매자연락처"),
    USER_NAME("구매자명"),
    SOCIAL_ORDER_ID("소셜주문아이디"),
    ORDER_ID("주문번호"),
    PRODUCT_ID("상품번호");

    private final String meaning;

    SearchCondition(String meaning) {
        this.meaning = meaning;
    }

    public static List<Display> getSearchableStates() {
        return Arrays.stream(SearchCondition.values())
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }


}

