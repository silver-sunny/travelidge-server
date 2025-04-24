package com.studio.core.order.dto.payment;

public record TemPaymentDto(
        String orderId,
        Long amount
) {
}
