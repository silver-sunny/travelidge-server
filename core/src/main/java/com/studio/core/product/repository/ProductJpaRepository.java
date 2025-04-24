package com.studio.core.product.repository;

import com.studio.core.product.entity.ProductEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {



    Optional<ProductEntity> findByChannelProductId(String productId);

}
