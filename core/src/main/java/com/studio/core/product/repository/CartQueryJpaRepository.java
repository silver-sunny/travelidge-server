package com.studio.core.product.repository;

import static com.studio.core.global.exception.ErrorCode.ORDER_CART_NOT_AVAILABLE;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.utils.QueryDslUtil;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.member.entity.QMemberAuthEntity;
import com.studio.core.order.dto.GetOrderFormResponseDto;
import com.studio.core.order.dto.QGetOrderFormResponseDto;
import com.studio.core.order.entity.QOrderEntity;
import com.studio.core.product.dto.cart.GetCartTableResponseDto;
import com.studio.core.product.entity.CartEntity;
import com.studio.core.product.entity.QCartEntity;
import com.studio.core.product.entity.QProductEntity;
import com.studio.core.product.entity.QProductImageEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CartQueryJpaRepository {

    private final JPAQueryFactory queryFactory;

    private static final QProductEntity productEntity = QProductEntity.productEntity;
    private static final QProductImageEntity productImageEntity = QProductImageEntity.productImageEntity;
    private static final QOrderEntity orderEntity = QOrderEntity.orderEntity;

    private static final QCartEntity cartEntity = QCartEntity.cartEntity;
private static final QMemberAuthEntity memberAuthEntity = QMemberAuthEntity.memberAuthEntity;

    public Page<GetCartTableResponseDto> getCarts(Pageable pageable, MemberAuthEntity member) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(cartEntity.member.eq(member));

        Long total = queryFactory
            .select(cartEntity.count())
            .from(cartEntity)
            .leftJoin(cartEntity.product, productEntity)
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        PathBuilder<CartEntity> entityPath = new PathBuilder<>(CartEntity.class, "cartEntity");

        List<GetCartTableResponseDto> content = queryFactory
            .select(Projections.constructor(
                GetCartTableResponseDto.class,
                cartEntity.id,
                cartEntity.product.id,
                cartEntity.purchaseQuantity,
                cartEntity.directOption,
                cartEntity.createAt,
                productEntity.pri,
                productEntity.price,
                productEntity.discountRate,
                productEntity.productName,
                productEntity.productState
            ))
            .from(cartEntity)
            .leftJoin(productEntity).on(productEntity.id.eq(cartEntity.product.id))
            .where(condition)
            .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);

    }
    public GetOrderFormResponseDto getMemberActiveOrderForm(MemberAuthEntity member) {
        return Optional.ofNullable(
            queryFactory
                .select(new QGetOrderFormResponseDto(
                    cartEntity.id,
                    cartEntity.purchaseQuantity,
                    cartEntity.directOption,
                    productEntity.id,
                    productEntity.pri,
                    productEntity.price,
                    productEntity.discountRate,
                    productEntity.productName,
                    productEntity.productState,
                    memberAuthEntity.name,
                    memberAuthEntity.phoneNumber))
                .from(cartEntity)
                .join(cartEntity.product, productEntity)
                .join(cartEntity.member, memberAuthEntity)
                .where(cartEntity.member.eq(member)
                    .and(cartEntity.isActiveOrderForm.isTrue()))
                .fetchOne()
        ).orElseThrow(() -> new CustomException(ORDER_CART_NOT_AVAILABLE));
    }

}
