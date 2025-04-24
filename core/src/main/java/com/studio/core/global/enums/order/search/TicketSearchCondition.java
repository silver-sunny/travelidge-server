package com.studio.core.global.enums.order.search;

import com.studio.core.global.repository.Display;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum TicketSearchCondition {
    PHONE_NUMBER("구매자연락처"),
    TICKET_KEY("티켓번호"),
    SOCIAL_ORDER_ID("소셜주문아이디"),
    ORDER_ID("주문번호");

    private final String meaning;

    TicketSearchCondition(String meaning) {
        this.meaning = meaning;
    }

    public static List<Display> getTicketSearchableStates() {
        return Arrays.stream(TicketSearchCondition.values())
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }


}

