package com.studio.api.product.service.serializer;

import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.cart.CartRequestDto;
import com.studio.core.product.entity.CartEntity;
import com.studio.core.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class CartSerializer {

    public static CartEntity toCartEntity(CartRequestDto dto, ProductEntity productEntity, MemberAuthEntity memberAuthEntity) {
        return CartEntity.builder()
            .purchaseQuantity(dto.purchaseQuantity())
            .directOption(dto.directOption())
            .isActiveOrderForm(true)
            .product(productEntity)
            .member(memberAuthEntity)
            .build();

    }



}
