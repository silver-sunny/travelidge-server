package com.studio.api.order.service.serializer;

import com.studio.core.global.enums.order.PaymentStatus;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.dto.payment.SaveAmountRequestDto;
import com.studio.core.order.entity.payment.PaymentEntity;
import com.studio.core.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentSerializer {

    public static PaymentEntity toPaymentEntity(SaveAmountRequestDto req, ProductEntity product,
        MemberAuthEntity member) {
        return PaymentEntity.builder()
            .product(product)
            .member(member)
            .totalAmount(req.purchasePrice())
            .productPrice(product.getPrice())
            .productDiscountRate(product.getDiscountRate())
            .status(PaymentStatus.READY)
            .purchaseQuantity(req.purchaseQuantity())
            .purchaseUserName(req.purchaseUserName())
            .phoneNumber(req.phoneNumber())
            .directOption(req.directOption())
            .build();

    }



}
