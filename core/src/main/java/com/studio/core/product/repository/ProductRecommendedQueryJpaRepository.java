package com.studio.core.product.repository;

//import static jdk.internal.jrtfs.JrtFileAttributeView.AttrID.size;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studio.core.global.enums.inquiry.InquiryPrivateState;
import com.studio.core.product.dto.product.GetClientRecommendedProductResponseDto;
import com.studio.core.product.dto.product.GetProductHomeTableResponseDto;
import com.studio.core.product.dto.product.QGetClientRecommendedProductResponseDto;
import com.studio.core.product.dto.product.QGetProductHomeTableResponseDto;
import com.studio.core.product.entity.QProductEntity;
import com.studio.core.product.entity.QProductFavoriteEntity;
import com.studio.core.product.entity.QProductRecommendedEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class ProductRecommendedQueryJpaRepository {

    private final JPAQueryFactory queryFactory;

    private static final QProductRecommendedEntity recommended = QProductRecommendedEntity.productRecommendedEntity;
    private static final QProductEntity product = QProductEntity.productEntity;
    private static final QProductFavoriteEntity favorite = QProductFavoriteEntity.productFavoriteEntity;

    // 추천 상품 리스트
//    public List<GetClientRecommendedProductResponseDto> findClientRecommendedProducts(Long memberNo) {
//        JPAQuery<GetClientRecommendedProductResponseDto> query = queryFactory
//                .select(new QGetClientRecommendedProductResponseDto(
//                        product.id,
//                        product.productName,
//                        product.price,
//                        product.discountRate,
//                        product.pri,
//                        memberNo != null ?
//                                Expressions.cases()
//                                        .when(favorite.id.isNotNull())
//                                        .then(true)
//                                        .otherwise(false)
//                                :
//                                Expressions.constant(false)
//                ))
//                .from(recommended)
//                .join(recommended.product, product);
//
//        if (memberNo != null) {
//            query.leftJoin(favorite)
//                    .on(favorite.product.id.eq(product.id)
//                            .and(favorite.member.memberNo.eq(memberNo)));
//        }
//
//        return query.fetch();
//    }
    public List<GetProductHomeTableResponseDto> findClientRecommendedProducts(Long memberNo,
        int size) {
        return queryFactory
            .select(new QGetProductHomeTableResponseDto(
                product.id,
                product.productName,
                product.price,
                product.discountRate,
                product.stock,
                product.productState,
                product.pri,
                memberNo != null ?
                    JPAExpressions.selectOne()
                        .from(favorite)
                        .where(favorite.product.id.eq(product.id)
                            .and(favorite.member.memberNo.eq(memberNo)))
                        .exists()
                    : Expressions.FALSE
            ))
            .from(recommended)
            .join(recommended.product, product)
            .leftJoin(favorite)
            .on(favorite.product.id.eq(product.id)
                .and(memberNo != null ? favorite.member.memberNo.eq(memberNo) : null))
            .limit(size)
            .fetch();
    }

}