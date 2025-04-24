package com.studio.core.product.repository;


import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.entity.CartEntity;
import com.studio.core.product.entity.ProductEntity;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {


    @Transactional
    @Modifying
    @Query("UPDATE CartEntity c "
        + "SET c.isActiveOrderForm = false "
        + "WHERE c.member.memberNo = :memberNo")
    void updateActiveForm(Long memberNo);

    @Query("SELECT c FROM CartEntity c JOIN FETCH c.member JOIN FETCH c.product WHERE c.member.memberNo = :memberNo AND c.product.id = :productId")
    Optional<CartEntity> findCartByProductAndMember(Long productId, Long memberNo);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartEntity c WHERE c.product.id = :productId AND c.member.memberNo = :memberNo")
    void deleteCartByProductAndMember(@Param("productId") Long productId, @Param("memberNo") Long memberNo);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartEntity c WHERE c.product.id = :productId")
    void deleteByProductId(@Param("productId")Long productId);

}
