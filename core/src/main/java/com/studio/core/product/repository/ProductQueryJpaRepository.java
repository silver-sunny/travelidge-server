package com.studio.core.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.FilterProductState;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.utils.QueryDslUtil;
import com.studio.core.order.entity.QOrderEntity;
import com.studio.core.product.dto.product.*;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.entity.QProductEntity;
import com.studio.core.product.entity.QProductFavoriteEntity;
import com.studio.core.product.entity.QProductImageEntity;
import com.studio.core.review.entity.QReviewEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class ProductQueryJpaRepository {

    private final JPAQueryFactory queryFactory;

    private static final QProductEntity productEntity = QProductEntity.productEntity;
    private static final QProductImageEntity productImageEntity = QProductImageEntity.productImageEntity;
    private static final QOrderEntity orderEntity = QOrderEntity.orderEntity;
    private static final QProductFavoriteEntity favorite = QProductFavoriteEntity.productFavoriteEntity;
    private static final QReviewEntity reviewEntity = QReviewEntity.reviewEntity;

    @Transactional(readOnly = true)
    public Page<GetProductTableResponseDto> getSalesProducts(Pageable pageable,
                                                             FilterProductState state, Channels channel) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(productEntity.productState.ne(ProductState.DELETE));

        ProductState filterState = (state != null) ? (state.equals(FilterProductState.ALL) ? null
                : ProductState.fromStateNo(state.getStateNo())) : null;
        if (filterState != null) {
            condition.and(productEntity.productState.eq(filterState));

        }

        Channels filterChannel = (channel != null) ? (channel.equals(Channels.ETC) ? null
                : Channels.getByIndex(channel.getIndex())) : null;
        if (filterChannel != null) {
            condition.and(productEntity.channel.eq(filterChannel));

        }

        Long total = queryFactory
                .select(productEntity.count())
                .from(productEntity)
                .where(condition)
                .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }
        PathBuilder<ProductEntity> entityPath = new PathBuilder<>(ProductEntity.class,
                "productEntity");

        List<GetProductTableResponseDto> content = queryFactory
                .select(new QGetProductTableResponseDto(
                        productEntity.id,
                        productEntity.productName,
                        productEntity.price,
                        productEntity.discountRate,
                        productEntity.stock,
                        productEntity.productState,
                        productEntity.channel,
                        productEntity.channelProductId,
                        productEntity.pri,
                        productEntity.description,
                        productEntity.createAt,
                        productEntity.updateAt
                ))
                .from(productEntity)
                .where(condition)
                .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        return new PageImpl<>(content, pageable, total);
    }


    public GetProductDetailResponseDto getProduct(Long productId) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(productEntity.productState.ne(ProductState.DELETE));
        condition.and(productEntity.id.eq(productId));

        return queryFactory.from(productEntity)
                .leftJoin(productImageEntity).on(productEntity.id.eq(productImageEntity.product.id))
                .where(condition)
                .transform(groupBy(productEntity).as(new QGetProductDetailResponseDto(
                                productEntity.id,
                                productEntity.productName,
                                productEntity.price,
                                productEntity.discountRate,
                                productEntity.stock,
                                productEntity.productState,
                                productEntity.channel,
                                productEntity.channelProductId,
                                productEntity.pri,
                                GroupBy.list(productImageEntity.imageUrl),
                                productEntity.description,
                                productEntity.createAt,
                                productEntity.updateAt
                        )
                )).values().stream().findFirst().orElse(null);

    }

    public GetProductClientDetailResponseDto getProduct(Long productId, Long memberNo) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(productEntity.id.eq(productId));

        return queryFactory.from(productEntity)
                .leftJoin(productImageEntity).on(productEntity.id.eq(productImageEntity.product.id))
                .leftJoin(favorite)
                .on(favorite.product.id.eq(productEntity.id)
                        .and(memberNo != null ? favorite.member.memberNo.eq(memberNo) : null))
                .where(condition)
                .transform(groupBy(productEntity).as(new QGetProductClientDetailResponseDto(
                                productEntity.id,
                                productEntity.productName,
                                productEntity.price,
                                productEntity.discountRate,
                                productEntity.stock,
                                productEntity.productState,
                                productEntity.channel,
                                productEntity.channelProductId,
                                productEntity.pri,
                                GroupBy.list(productImageEntity.imageUrl),
                                productEntity.description,
                                productEntity.createAt,
                                productEntity.updateAt,
                                memberNo != null ?
                                        Expressions.cases()
                                                .when(favorite.id.isNotNull())
                                                .then(true)
                                                .otherwise(false)
                                        :
                                        Expressions.constant(false)

                        )
                )).values().stream().findFirst().orElse(null);

    }


    public Page<GetProductHomeTableResponseDto> getBestProductMore(Long memberNo,
                                                                   Pageable pageable) {
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(productEntity.channel.eq(Channels.TRAVELIDGE));
        condition.and(productEntity.productState.eq(ProductState.SALE));

        Long total = queryFactory
                .select(productEntity.count())
                .from(productEntity)
                .where(condition)
                .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }


        List<GetProductHomeTableResponseDto> content = queryFactory
                .select(new QGetProductHomeTableResponseDto(
                        productEntity.id,
                        productEntity.productName,
                        productEntity.price,
                        productEntity.discountRate,
                        productEntity.stock,
                        productEntity.productState,
                        productEntity.pri,
                        memberNo != null ?
                                Expressions.cases()
                                        .when(
                                                JPAExpressions
                                                        .selectOne()
                                                        .from(favorite)
                                                        .where(favorite.product.id.eq(productEntity.id)
                                                                .and(favorite.member.memberNo.eq(memberNo)))
                                                        .exists()
                                        ).then(true)
                                        .otherwise(false)
                                : Expressions.constant(false)
                ))
                .from(productEntity)
                .leftJoin(orderEntity).on(orderEntity.product.id.eq(productEntity.id))
                .where(condition)
                .groupBy(
                        productEntity.id,
                        productEntity.productName,
                        productEntity.price,
                        productEntity.discountRate,
                        productEntity.stock,
                        productEntity.productState,
                        productEntity.pri
                )
                .orderBy(orderEntity.id.count().desc(), productEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);

    }

    public List<GetProductHomeTableResponseDto> getBestProduct(Long memberNo, int size) {
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(productEntity.channel.eq(Channels.TRAVELIDGE));
        condition.and(productEntity.productState.eq(ProductState.SALE));

        return queryFactory
                .select(new QGetProductHomeTableResponseDto(
                        productEntity.id,
                        productEntity.productName,
                        productEntity.price,
                        productEntity.discountRate,
                        productEntity.stock,
                        productEntity.productState,
                        productEntity.pri,
                        memberNo != null ?
                                Expressions.cases()
                                        .when(
                                                JPAExpressions
                                                        .selectOne()
                                                        .from(favorite)
                                                        .where(favorite.product.id.eq(productEntity.id)
                                                                .and(favorite.member.memberNo.eq(memberNo)))
                                                        .exists()
                                        ).then(true)
                                        .otherwise(false)
                                : Expressions.constant(false)
                ))
                .from(productEntity)
                .leftJoin(orderEntity).on(orderEntity.product.id.eq(productEntity.id))
                .where(condition)
                .groupBy(
                        productEntity.id,
                        productEntity.productName,
                        productEntity.price,
                        productEntity.discountRate,
                        productEntity.stock,
                        productEntity.productState,
                        productEntity.pri
                )
                .orderBy(orderEntity.id.count().desc(), productEntity.id.desc())
                .limit(size)
                .fetch();
    }


    public List<GetProductHomeTableResponseDto> getHomeProducts(Long memberNo, int size) {
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(productEntity.channel.eq(Channels.TRAVELIDGE));
        condition.and(productEntity.productState.eq(ProductState.SALE));

        return queryFactory
                .select(new QGetProductHomeTableResponseDto(
                        productEntity.id,
                        productEntity.productName,
                        productEntity.price,
                        productEntity.discountRate,
                        productEntity.stock,
                        productEntity.productState,
                        productEntity.pri,
                        memberNo != null ?
                                Expressions.cases()
                                        .when(favorite.id.isNotNull())
                                        .then(true)
                                        .otherwise(false)
                                :
                                Expressions.constant(false)
                ))
                .from(productEntity)
                .leftJoin(favorite)
                .on(favorite.product.id.eq(productEntity.id)
                        .and(memberNo != null ? favorite.member.memberNo.eq(memberNo) : null))
                .where(condition)
                .orderBy(productEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public Page<GetProductHomeTableResponseDto> getMoreProducts(Long memberNo, Pageable pageable) {
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(productEntity.channel.eq(Channels.TRAVELIDGE));
        condition.and(productEntity.productState.eq(ProductState.SALE));

        // 총 개수 조회
        Long total = queryFactory
                .select(productEntity.count())
                .from(productEntity)
                .where(condition)
                .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        PathBuilder<ProductEntity> entityPath = new PathBuilder<>(ProductEntity.class,
                "productEntity");

        List<GetProductHomeTableResponseDto> content = queryFactory
                .select(new QGetProductHomeTableResponseDto(
                        productEntity.id,
                        productEntity.productName,
                        productEntity.price,
                        productEntity.discountRate,
                        productEntity.stock,
                        productEntity.productState,
                        productEntity.pri, // 상품 대표 이미지
                        memberNo != null ?
                                Expressions.cases()
                                        .when(favorite.id.isNotNull())
                                        .then(true)
                                        .otherwise(false)
                                :
                                Expressions.constant(false)
                ))
                .from(productEntity)
                .leftJoin(favorite)
                .on(favorite.product.id.eq(productEntity.id)
                        .and(memberNo != null ? favorite.member.memberNo.eq(memberNo) : null))
                .where(condition)
                .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);

    }

    public Page<GetProductHomeTableResponseDto> getSearchProducts(String keyword,
                                                                  Long memberNo, Pageable pageable) {
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(productEntity.channel.eq(Channels.TRAVELIDGE));
        condition.and(productEntity.productState.eq(ProductState.SALE));
        condition.and(productEntity.productName.containsIgnoreCase(keyword));

        // 총 개수 조회
        Long total = queryFactory
                .select(productEntity.count())
                .from(productEntity)
                .where(condition)
                .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        List<GetProductHomeTableResponseDto> content = queryFactory
                .select(new QGetProductHomeTableResponseDto(
                        productEntity.id,
                        productEntity.productName,
                        productEntity.price,
                        productEntity.discountRate,
                        productEntity.stock,
                        productEntity.productState,
                        productEntity.pri, // 상품 대표 이미지
                        memberNo != null ?
                                Expressions.cases()
                                        .when(favorite.id.isNotNull())
                                        .then(true)
                                        .otherwise(false)
                                :
                                Expressions.constant(false)
                ))
                .from(productEntity)
                .leftJoin(favorite)
                .on(favorite.product.id.eq(productEntity.id)
                        .and(memberNo != null ? favorite.member.memberNo.eq(memberNo) : null))
                .where(condition)
                .orderBy(productEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);

    }

    public List<GetProductSortReviewTableResponseDto> getSortReviewProduct(int size) {
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(productEntity.channel.eq(Channels.TRAVELIDGE));
        condition.and(productEntity.productState.eq(ProductState.SALE));

        NumberTemplate<Double> avgRating = Expressions.numberTemplate(
                Double.class,
                "round(avg({0}), 1)",
                reviewEntity.rating
        );

        return queryFactory
                .select(new QGetProductSortReviewTableResponseDto(
                        productEntity.id,
                        productEntity.productName,
                        productEntity.price,
                        productEntity.discountRate,
                        productEntity.productState,
                        productEntity.pri,
                        avgRating
                ))
                .from(reviewEntity)
                .join(productEntity).on(productEntity.id.eq(reviewEntity.product.id))
                .where(condition)
                .groupBy(productEntity)
                .orderBy(reviewEntity.rating.avg().desc(), productEntity.id.desc()) // 평균 별점 기준 정렬
                .limit(size)
                .fetch();
    }


}
