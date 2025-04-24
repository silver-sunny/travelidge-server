package com.studio.core.global.enums.converter;

import com.studio.core.global.enums.order.TicketUsedState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter
public class TicketUsedStateConverter implements AttributeConverter<TicketUsedState, Boolean> {


    @Override
    public Boolean convertToDatabaseColumn(TicketUsedState ticketUsedState) {
        return Optional.ofNullable(ticketUsedState).map(TicketUsedState::getState).orElse(null);
    }

    @Override
    public TicketUsedState convertToEntityAttribute(Boolean aBoolean) {
        return TicketUsedState.getByState(aBoolean);

    }
}
