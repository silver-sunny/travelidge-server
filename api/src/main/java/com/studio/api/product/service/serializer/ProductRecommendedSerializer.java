package com.studio.api.product.service.serializer;

import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.entity.ProductRecommendedEntity;

public class ProductRecommendedSerializer {
    public static ProductRecommendedEntity toEntity(ProductEntity product) {
        return ProductRecommendedEntity.builder()
                .product(product)
                .build();
    }

}
