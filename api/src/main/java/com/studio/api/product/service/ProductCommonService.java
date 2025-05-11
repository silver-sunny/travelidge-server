package com.studio.api.product.service;

import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.order.repository.OrderJpaRepository;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCommonService {

    private final ProductJpaRepository productJpaRepository;
    private final OrderJpaRepository orderJpaRepository;

    public void increaseProductStock(int purchaseQuantity, Long productId) {

        ProductEntity product = productJpaRepository.findById(productId)
            .orElseThrow(() -> new CustomException(
                ErrorCode.PRODUCT_NOT_FOUND));

        product.increaseStock((long) purchaseQuantity);

        productJpaRepository.save(product);

    }


    public void decreaseProductStock(int purchaseQuantity, Long productId) {

        ProductEntity product = productJpaRepository.findById(productId)
            .orElseThrow(() -> new CustomException(
                ErrorCode.PRODUCT_NOT_FOUND));

        product.decreaseStock((long) purchaseQuantity);

        productJpaRepository.save(product);

    }
}
