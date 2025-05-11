package com.studio.core.order.dto.payment;

import jakarta.validation.constraints.NotNull;

public record SaveAmountRequestDto(

    @NotNull
    Long productId,

    @NotNull
    Long purchasePrice,
    
    int purchaseQuantity,

    @NotNull
    String phoneNumber,

    @NotNull
    String purchaseUserName,

    @NotNull
    String directOption
) {

}
