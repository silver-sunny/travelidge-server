package com.studio.core.global.enums.converter;

import com.studio.core.global.enums.order.TicketState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter
public class TicketStateConverter implements AttributeConverter<TicketState,Integer > {
    @Override
    public Integer convertToDatabaseColumn(TicketState state) {
        return Optional.ofNullable(state).map(TicketState::getIndex).orElse(null);
    }

    @Override
    public TicketState convertToEntityAttribute(Integer index) {
        return TicketState.fromIndex(index);
    }
}
