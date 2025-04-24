package com.studio.core.global.enums.order;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum TicketState {
    ETC(-1, ""),
    NON_ISSUED(0, "발급 전"),
    AVAILABLE(1, "발급완료");

    private final Integer index;
    private final String meaning;

    TicketState(Integer index, String meaning) {
        this.index = index;
        this.meaning = meaning;
    }

    public static TicketState fromIndex(Integer index) {
        return Arrays.stream(TicketState.values())
                .filter(e -> e.index.equals(index))
                .findFirst()
                .orElse(ETC);
    }
}
