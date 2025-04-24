package com.studio.core.global.valid.interfaces;

import com.studio.core.global.valid.validator.PriceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceValidator.class)
@Documented
public @interface PriceValid {
    String message() default "가격은 10의 배수여야합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
