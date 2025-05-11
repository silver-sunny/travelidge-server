package com.studio.core.global.valid.validator;

import com.studio.core.global.valid.interfaces.PriceValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<PriceValid, Long> {

    @Override
    public boolean isValid(Long price, ConstraintValidatorContext context) {
        if (price == null) {
            return false; // 가격이 null이면 실패
        }
        return price >= 1000 && price <= 10000000 && price % 10 == 0;
    }
}

