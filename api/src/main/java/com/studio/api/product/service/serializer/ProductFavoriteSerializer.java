package com.studio.api.product.service.serializer;

import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.entity.ProductFavoriteEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductFavoriteSerializer {
    public static ProductFavoriteEntity toFavoriteEntity(ProductEntity product, MemberAuthEntity member) {
        return ProductFavoriteEntity.builder()
                .product(product)
                .member(member)
                .build();
    }
}
