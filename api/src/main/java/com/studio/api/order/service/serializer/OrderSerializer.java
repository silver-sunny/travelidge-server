package com.studio.api.order.service.serializer;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketState;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.entity.payment.PaymentEntity;
import com.studio.core.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderSerializer {

    public static OrderEntity toOrderEntity(Channels channels,PaymentEntity payment, ProductEntity product, MemberAuthEntity memberAuth) {
        return OrderEntity.builder()
            .channel(channels)
            .channelProductOrderId(payment.getChannelProductOrderId())
            .orderState(ProductOrderState.getOrderStateByPayment(payment.getStatus()))
            .purchaseAt(payment.getApprovedAt())
            .purchasePrice(payment.getTotalAmount())
            .purchaseQuantity(payment.getPurchaseQuantity())
            .purchaseUserName(payment.getPurchaseUserName())
            .phoneNumber(payment.getPhoneNumber())
            .directOption(payment.getDirectOption())
            .shippingDueDate(payment.getApprovedAt() == null ? null : payment.getApprovedAt().plusDays(4))

            .member(payment.getMember())
            .product(payment.getProduct())
            .ticketState(TicketState.NON_ISSUED)
            .isProgressing(false)
            .product(product)
            .member(memberAuth)
            .build();

    }


}
