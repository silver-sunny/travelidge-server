package com.studio.core.global.utils;

public class CalculatorUtil {

    public static Long calculateDiscountPrice(Long discountRate, Long price) {
        if (discountRate != null && discountRate > 0) {
            double discountMultiplier = 1 - (discountRate / 100.0);
            double discountedPrice = price * discountMultiplier;
            return Math.round(discountedPrice);
        } else {
            return price; // 할인율이 0이거나 null이면 원 가격 반환
        }
    }
}
