package com.studio.core.global.enums.order;

import lombok.Getter;

@Getter
public enum TicketUsedState {
    NOT_CONFIRM(null,"사용 불가능"),
    USED(true, "사용완료"),
    AVAILABLE(false, "사용가능");

    private final Boolean state;
    private final String meaning;

    TicketUsedState(Boolean state, String meaning) {
        this.state = state;
        this.meaning = meaning;
    }

    public static TicketUsedState getByState(Boolean state) {
        for (TicketUsedState ticketUsedState : TicketUsedState.values()) {
            if (ticketUsedState.state == state) {
                return ticketUsedState;
            }
        }
        return TicketUsedState.NOT_CONFIRM;
    }

}
