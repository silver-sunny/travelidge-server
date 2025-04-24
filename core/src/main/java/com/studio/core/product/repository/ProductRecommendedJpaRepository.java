package com.studio.core.product.repository;

import com.studio.core.product.entity.ProductRecommendedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRecommendedJpaRepository extends JpaRepository<ProductRecommendedEntity, Long> {

    // 추천 상품 전체 조회
    @Query("SELECT r FROM ProductRecommendedEntity r JOIN FETCH r.product ORDER BY r.id ASC")
    List<ProductRecommendedEntity> findAllByOrderByIdAsc();

    // 이미 추천된 상품인지 확인
    boolean existsByProductId(Long productId);

    // 상품 ID로 추천 상품 삭제
    void deleteByProductId(Long productId);
}

