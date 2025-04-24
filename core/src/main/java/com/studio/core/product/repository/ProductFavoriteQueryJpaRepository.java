package com.studio.core.product.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studio.core.member.entity.QMemberAuthEntity;
import com.studio.core.product.dto.product.GetFavoriteProductResponseDto;
import com.studio.core.product.dto.product.GetProductHomeTableResponseDto;
import com.studio.core.product.dto.product.QGetFavoriteProductResponseDto;
import com.studio.core.product.dto.product.QGetProductHomeTableResponseDto;
import com.studio.core.product.entity.QProductEntity;
import com.studio.core.product.entity.QProductFavoriteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductFavoriteQueryJpaRepository {

    private final JPAQueryFactory queryFactory;

    private static final QProductFavoriteEntity favorite = QProductFavoriteEntity.productFavoriteEntity;
    private static final QProductEntity product = QProductEntity.productEntity;
    private static final QMemberAuthEntity member = QMemberAuthEntity.memberAuthEntity;

    @Transactional(readOnly = true)
    public Page<GetProductHomeTableResponseDto> findFavoritesByMember(Long memberNo,
        Pageable pageable) {

        Long total = queryFactory
            .select(favorite.count())
            .from(favorite)
            .where(favorite.member.memberNo.eq(memberNo))
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        // 페이징 목록
        List<GetProductHomeTableResponseDto> content = queryFactory
            .select(new QGetProductHomeTableResponseDto(
                product.id,
                product.productName,
                product.price,
                product.discountRate,
                product.stock,
                product.productState,
                product.pri, // 상품 대표 이미지
                Expressions.constant(true)
            ))
            .from(favorite)
            .join(favorite.product, product)
            .where(favorite.member.memberNo.eq(memberNo))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    public boolean isFavorite(Long productId, Long memberNo) {
        Integer exists = queryFactory
            .selectOne()
            .from(favorite)
            .where(
                favorite.product.id.eq(productId),
                favorite.member.memberNo.eq(memberNo)
            )
            .fetchFirst();

        return exists != null;
    }

    public List<Long> findFavoriteProductId(Long memberNo, List<Long> productId) {
        return queryFactory
            .select(favorite.product.id)
            .from(favorite)
            .where(
                favorite.member.memberNo.eq(memberNo),
                favorite.product.id.in(productId)
            )
            .fetch();
    }
}