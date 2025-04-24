package com.studio.core.global.enums.converter;

import com.studio.core.global.enums.order.payment.PaymentCardCompany;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class PaymentCartCompanyConverter implements AttributeConverter<PaymentCardCompany, String> {

    @Override
    public String convertToDatabaseColumn(PaymentCardCompany paymentCardCompany) {


        return Optional.ofNullable(paymentCardCompany).map(PaymentCardCompany::getCode).orElse(null);

    }

    @Override
    public PaymentCardCompany convertToEntityAttribute(String code) {

        return PaymentCardCompany.fromCode(code);
    }
}
