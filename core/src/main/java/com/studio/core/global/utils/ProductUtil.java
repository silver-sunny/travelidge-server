package com.studio.core.global.utils;

import com.studio.core.product.dto.product.naver.NaverCustomerBenefitDto;

public class ProductUtil {

    public static Long getValueFromDiscountPolicy(NaverCustomerBenefitDto naverOriginProduct) {
        if (naverOriginProduct.getImmediateDiscountPolicy() != null
            && naverOriginProduct.getImmediateDiscountPolicy().getDiscountMethod() != null) {
            return naverOriginProduct.getImmediateDiscountPolicy().getDiscountMethod().getValue();
        }
        return null;
    }

}
