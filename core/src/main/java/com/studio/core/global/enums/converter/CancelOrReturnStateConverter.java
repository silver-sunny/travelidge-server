package com.studio.core.global.enums.converter;

import com.studio.core.global.enums.order.CancelOrReturnState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class CancelOrReturnStateConverter implements AttributeConverter<CancelOrReturnState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CancelOrReturnState cancelOrReturnState) {


        return Optional.ofNullable(cancelOrReturnState).map(CancelOrReturnState::getIndex).orElse(null);

    }

    @Override
    public CancelOrReturnState convertToEntityAttribute(Integer index) {

        return CancelOrReturnState.fromIndex(index);
    }
}
