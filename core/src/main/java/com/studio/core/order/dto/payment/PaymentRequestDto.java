package com.studio.core.order.dto.payment;

public record PaymentRequestDto(String orderId,
                                Long amount,
                                String paymentKey
                                ) {
}
