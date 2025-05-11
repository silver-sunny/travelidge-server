package com.studio.core.global.enums.converter;

import com.studio.core.global.enums.ProductState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter
public class ProductStateConverter implements AttributeConverter<ProductState, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductState state) {
        return Optional.ofNullable(state).map(ProductState::getStateNo).orElse(null);

    }

    @Override
    public ProductState convertToEntityAttribute(Integer stateNo) {
        return ProductState.fromStateNo(stateNo);

    }
}
