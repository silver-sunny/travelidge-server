package com.studio.api.product.service;

import static com.studio.api.product.service.serializer.CartSerializer.toCartEntity;
import static com.studio.core.global.exception.ErrorCode.CART_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.NOT_VALID_MEMBER;
import static com.studio.core.global.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.PRODUCT_NOT_VALID_ORDER_STATE;
import static com.studio.core.global.exception.ErrorCode.PRODUCT_NOT_VALID_STATE;
import static com.studio.core.global.exception.ErrorCode.STOCK_OVER;

import com.studio.core.global.enums.ProductState;
import com.studio.core.global.exception.CustomException;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.dto.cart.CartRequestDto;
import com.studio.core.product.entity.CartEntity;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.CartJpaRepository;
import com.studio.core.product.repository.ProductJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartJpaRepository cartRepository;
private final ProductJpaRepository productRepository;

    public void upsertCart(CartRequestDto requestDto, MemberAuthEntity member) {

        ProductEntity product = productRepository.findById(requestDto.productId()).orElseThrow(()->new CustomException(PRODUCT_NOT_FOUND));

        if(ProductState.DELETE.equals(product.getProductState())){
            cartRepository.deleteCartByProductAndMember(product.getId(), member.getMemberNo());
            throw new CustomException(PRODUCT_NOT_VALID_ORDER_STATE);
        }

        if(!ProductState.SALE.equals(product.getProductState())){
            throw new CustomException(PRODUCT_NOT_VALID_STATE);
        }
        if(product.getStock() < requestDto.purchaseQuantity()){
            throw new CustomException(STOCK_OVER);

        }
        Optional<CartEntity> cart = cartRepository.findCartByProductAndMember(product.getId(), member.getMemberNo());
        cartRepository.updateActiveForm(member.getMemberNo());

        if (cart.isPresent()) {
            CartEntity existingCart = cart.get();
            existingCart.updateCart(requestDto.purchaseQuantity(), requestDto.directOption());
            cartRepository.save(existingCart);
        } else {
            cartRepository.save(toCartEntity(requestDto, product, member));
        }
    }
    public void deleteCart(Long id, MemberAuthEntity member) {

        CartEntity cart = cartRepository.findById(id).orElseThrow(()->new CustomException(CART_NOT_FOUND));


        if(!cart.getMember().getMemberNo().equals(member.getMemberNo())){
            throw new CustomException(NOT_VALID_MEMBER);
        }

        cartRepository.deleteById(id);
    }

}
