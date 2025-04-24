package com.studio.api.product.service.serializer;

import com.studio.core.product.dto.product.ProductSearchResponseDto;
import com.studio.core.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductSearchSerializer {

    public ProductSearchResponseDto toDto(ProductEntity productEntity) {
        return ProductSearchResponseDto.builder()
            .productName(productEntity.getProductName())
            .productId(productEntity.getId())
            .productState(productEntity.getProductState().getMeaning())
            .channel(productEntity.getChannel().getMeaning())
            .build();
    }
}