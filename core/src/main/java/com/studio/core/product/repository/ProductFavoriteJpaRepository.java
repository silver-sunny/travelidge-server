package com.studio.core.product.repository;

import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.entity.ProductFavoriteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFavoriteJpaRepository extends JpaRepository<ProductFavoriteEntity, Long> {
    // 관심상품 여부
    boolean existsByProductAndMember(ProductEntity product, MemberAuthEntity member);

    // 관심상품 목록 조회
    Page<ProductFavoriteEntity> findByMember(MemberAuthEntity member, Pageable pageable);

    // 관심상품 삭제
    void deleteByProductAndMember(ProductEntity product, MemberAuthEntity member);

    // 관심상품 수 조회
    long countById(Long productId);

    void deleteByProductId(Long productId);

}