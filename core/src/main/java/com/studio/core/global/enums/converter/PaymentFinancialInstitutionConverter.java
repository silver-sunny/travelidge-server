package com.studio.core.global.enums.converter;

import com.studio.core.global.enums.order.payment.PaymentFinancialInstitution;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class PaymentFinancialInstitutionConverter implements AttributeConverter<PaymentFinancialInstitution, String> {

    @Override
    public String convertToDatabaseColumn(PaymentFinancialInstitution institution) {


        return Optional.ofNullable(institution).map(PaymentFinancialInstitution::getCode).orElse(null);

    }

    @Override
    public PaymentFinancialInstitution convertToEntityAttribute(String code) {

        return PaymentFinancialInstitution.fromCode(code);
    }
}
