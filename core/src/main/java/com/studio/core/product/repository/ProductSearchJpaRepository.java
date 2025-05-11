package com.studio.core.product.repository;


import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.ProductState;
import com.studio.core.product.entity.ProductEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSearchJpaRepository extends JpaRepository<ProductEntity, Long> {

    // 상품명에 포함된 검색어
    List<ProductEntity> findByProductNameContainingAndProductStateAndChannel(
        String productName,
        ProductState productState,
        Channels channel
    );

}
