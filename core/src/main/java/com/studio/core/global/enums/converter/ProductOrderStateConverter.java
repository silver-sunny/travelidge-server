package com.studio.core.global.enums.converter;

import com.studio.core.global.enums.order.ProductOrderState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter
public class ProductOrderStateConverter implements AttributeConverter<ProductOrderState, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductOrderState productOrderState) {
        return Optional.ofNullable(productOrderState).map(ProductOrderState::getIndex).orElse(null);

    }

    @Override
    public ProductOrderState convertToEntityAttribute(Integer index) {

        return ProductOrderState.fromIndex(index);
    }
}
