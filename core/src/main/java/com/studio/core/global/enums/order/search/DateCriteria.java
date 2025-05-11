package com.studio.core.global.enums.order.search;

import com.studio.core.global.repository.Display;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;


@Getter
public enum DateCriteria {
    PAYMENT_DATE("결제일"),
    CANCEL_REQUEST_DATE("취소/반품 요청일"),
    REFUND_DATE("환불일");

    private final String meaning;

    DateCriteria(String meaning) {
        this.meaning = meaning;
    }


    public static List<Display> getSearchableStates() {
        return Arrays.stream(DateCriteria.values())
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }



    public static List<Display> getNonTicketSearchableStates() {
        return Arrays.stream(DateCriteria.values())
                .filter(state -> List.of(
                        PAYMENT_DATE
                ).contains(state))
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }


    public static List<Display> getReturnRequestSearchableStates() {
        return Arrays.stream(DateCriteria.values())
                .filter(state -> List.of(
                        CANCEL_REQUEST_DATE,PAYMENT_DATE
                ).contains(state))
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }

    public static List<Display> getReturnCompleteSearchableStates() {
        return Arrays.stream(DateCriteria.values())
                .filter(state -> List.of(
                        REFUND_DATE,PAYMENT_DATE
                ).contains(state))
                .map(value -> new Display(value.name(), value.getMeaning()))
                .toList();
    }

}

