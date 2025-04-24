package com.studio.core.order.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketState;
import com.studio.core.global.enums.order.search.CancelReturnStateCondition;
import com.studio.core.global.enums.order.search.DateCriteria;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.global.utils.QueryDslUtil;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.member.entity.QMemberAuthEntity;
import com.studio.core.order.dto.AllProductOrdersSearchDto;
import com.studio.core.order.dto.GetClientOrderTableResponseDto;
import com.studio.core.order.dto.GetClientOrderDetailResponseDto;
import com.studio.core.order.dto.GetProductOrderCancelOrReturnCompleteTableResponseDto;
import com.studio.core.order.dto.GetProductOrderCancelOrReturnRequestTableResponseDto;
import com.studio.core.order.dto.GetProductOrderNonTicketTableResponseDto;
import com.studio.core.order.dto.GetProductOrderSummaryDto;
import com.studio.core.order.dto.GetProductOrderTableResponseDto;
import com.studio.core.order.dto.ProductOrdersNonTicketSearchDto;
import com.studio.core.order.dto.ProductOrdersReturnCompleteSearchDto;
import com.studio.core.order.dto.ProductOrdersReturnRequestSearchDto;
import com.studio.core.order.dto.QGetClientOrderDetailResponseDto;
import com.studio.core.order.dto.QGetClientOrderTableResponseDto;
import com.studio.core.order.dto.QGetProductOrderCancelOrReturnCompleteTableResponseDto;
import com.studio.core.order.dto.QGetProductOrderCancelOrReturnRequestTableResponseDto;
import com.studio.core.order.dto.QGetProductOrderNonTicketTableResponseDto;
import com.studio.core.order.dto.QGetProductOrderSummaryDto;
import com.studio.core.order.dto.QGetProductOrderTableResponseDto;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.entity.QOrderEntity;
import com.studio.core.order.entity.payment.QPaymentEntity;
import com.studio.core.product.entity.QCartEntity;
import com.studio.core.product.entity.QProductEntity;
import com.studio.core.product.entity.QProductImageEntity;
import com.studio.core.ticket.entity.QTicketEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryJpaRepository {

    private final JPAQueryFactory queryFactory;

    private static final QProductEntity productEntity = QProductEntity.productEntity;
    private static final QProductImageEntity productImageEntity = QProductImageEntity.productImageEntity;
    private static final QOrderEntity orderEntity = QOrderEntity.orderEntity;

    private static final QCartEntity cartEntity = QCartEntity.cartEntity;
    private static final QMemberAuthEntity memberAuthEntity = QMemberAuthEntity.memberAuthEntity;
    private static final QTicketEntity ticketEntity = QTicketEntity.ticketEntity;

    private static final QPaymentEntity paymentEntity = QPaymentEntity.paymentEntity;


    public Page<GetProductOrderTableResponseDto> getProductOrderList(Pageable pageable,
        AllProductOrdersSearchDto searchDto) {

        BooleanBuilder condition = new BooleanBuilder();

        // 날짜 필터링 조건 추가
        if (searchDto.startDate() != null && searchDto.endDate() != null
            && searchDto.dateCriteria() != null) {
            condition.and(_getDateCriteriaCondition(searchDto.dateCriteria(),
                searchDto.startDate(),
                searchDto.endDate()));
        }

        // 상세 검색 조건 추가
        if (searchDto.searchCondition() != null && searchDto.searchKeyword() != null
            && !searchDto.searchKeyword().isEmpty()) {
            switch (searchDto.searchCondition()) {
                case PHONE_NUMBER:
                    condition.and(orderEntity.phoneNumber.contains(searchDto.searchKeyword()));
                    break;
                case USER_NAME:
                    condition.and(orderEntity.purchaseUserName.contains(searchDto.searchKeyword()));
                    break;
                case SOCIAL_ORDER_ID:
                    condition.and(orderEntity.channelOrderId.contains(searchDto.searchKeyword()));
                    break;
                case ORDER_ID:
                    if (NumberUtils.isCreatable(searchDto.searchKeyword())) {
                        Long orderId = Long.valueOf(searchDto.searchKeyword());
                        condition.and(orderEntity.id.eq(orderId));
                    } else {
                        // 숫자가 아니면 조건을 아예 걸지 않음 (전체 리스트를 출력하지 않게)
                        condition.and(orderEntity.id.isNull());
                    }
                    break;
                case PRODUCT_ID:
                    if (NumberUtils.isCreatable(searchDto.searchKeyword())) {
                        Long productId = Long.valueOf(searchDto.searchKeyword());
                        condition.and(productEntity.id.eq(productId));
                    } else {
                        // 숫자가 아니면 조건을 아예 걸지 않음 (전체 리스트를 출력하지 않게)
                        condition.and(productEntity.id.isNull());
                    }
                    break;
            }
        }

        if (searchDto.productOrderState() != null && searchDto.productOrderState().isSearchable()) {
            condition.and(orderEntity.orderState.eq(searchDto.productOrderState()));
        }

        if (searchDto.cancelOrReturnState() != null && searchDto.cancelOrReturnState()
            .isSearchable()) {
            condition.and(orderEntity.cancelOrReturnState.eq(searchDto.cancelOrReturnState()));
        }

        // 총 개수 조회
        Long total = queryFactory
            .select(orderEntity.count())
            .from(orderEntity)
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        // 데이터 조회
        List<GetProductOrderTableResponseDto> content = queryFactory
            .select(new QGetProductOrderTableResponseDto(
                    orderEntity.id,
                    orderEntity.channel,
                    orderEntity.channelOrderId,
                    orderEntity.channelProductOrderId,
                    orderEntity.orderState,
                    orderEntity.ticketState,
                    orderEntity.cancelOrReturnState,
                    orderEntity.purchaseAt,
                    orderEntity.product.id,
                    productEntity.productName,
                    orderEntity.directOption,
                    orderEntity.purchaseQuantity,
                    orderEntity.purchaseUserName,
                    orderEntity.phoneNumber,
                    orderEntity.purchaseUserId,
                    productEntity.channelProductId,
                    orderEntity.cancelOrReturnReason,
                    orderEntity.cancelOrReturnDetailReason,
                    ticketEntity.isUsed

                )

            )
            .from(orderEntity)
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .leftJoin(ticketEntity).on(orderEntity.id.eq(ticketEntity.order.id))
            .where(condition)
            .orderBy(orderEntity.id.desc())
            .offset(pageable.getOffset())  // 페이지 시작 위치
            .limit(pageable.getPageSize()) // 페이지 크기
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }


    public Page<GetProductOrderNonTicketTableResponseDto> getProductOrderNonTicketList(
        Pageable pageable, ProductOrdersNonTicketSearchDto searchDto) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(orderEntity.ticketState.eq(TicketState.NON_ISSUED));
        condition.and(orderEntity.orderState.eq(ProductOrderState.PAYED));

        // 날짜 필터링 조건 추가
        if (searchDto.startDate() != null && searchDto.endDate() != null
            && searchDto.dateCriteria() != null) {
            condition.and(_getDateCriteriaCondition(searchDto.dateCriteria(),
                searchDto.startDate(),
                searchDto.endDate()));
        }

        // 상세 검색 조건 추가
        if (searchDto.searchCondition() != null && searchDto.searchKeyword() != null
            && !searchDto.searchKeyword().isEmpty()) {
            switch (searchDto.searchCondition()) {
                case PHONE_NUMBER:
                    condition.and(orderEntity.phoneNumber.contains(searchDto.searchKeyword()));
                    break;
                case USER_NAME:
                    condition.and(orderEntity.purchaseUserName.contains(searchDto.searchKeyword()));
                    break;
                case SOCIAL_ORDER_ID:
                    condition.and(orderEntity.channelOrderId.contains(searchDto.searchKeyword()));
                    break;
                case ORDER_ID:
                    if (NumberUtils.isCreatable(searchDto.searchKeyword())) {
                        Long orderId = Long.valueOf(searchDto.searchKeyword());
                        condition.and(orderEntity.id.eq(orderId));
                    } else {
                        // 숫자가 아니면 조건을 아예 걸지 않음 (전체 리스트를 출력하지 않게)
                        condition.and(orderEntity.id.isNull());
                    }
                    break;
                case PRODUCT_ID:
                    if (NumberUtils.isCreatable(searchDto.searchKeyword())) {
                        Long productId = Long.valueOf(searchDto.searchKeyword());
                        condition.and(productEntity.id.eq(productId));
                    } else {
                        // 숫자가 아니면 조건을 아예 걸지 않음 (전체 리스트를 출력하지 않게)
                        condition.and(productEntity.id.isNull());
                    }
                    break;
            }
        }

        // 총 개수 조회
        Long total = queryFactory
            .select(orderEntity.count())
            .from(orderEntity)
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        // 데이터 조회
        List<GetProductOrderNonTicketTableResponseDto> content = queryFactory
            .select(new QGetProductOrderNonTicketTableResponseDto(
                    orderEntity.id,
                    orderEntity.channel,
                    orderEntity.channelOrderId,
                    orderEntity.channelProductOrderId,
                    orderEntity.orderState,
                    orderEntity.ticketState,
                    orderEntity.purchaseAt,
                    orderEntity.shippingDueDate,
                    orderEntity.product.id,
                    productEntity.productName,
                    orderEntity.directOption,
                    orderEntity.purchaseQuantity,
                    orderEntity.purchaseUserName,
                    orderEntity.phoneNumber,
                    orderEntity.purchaseUserId,
                    productEntity.channelProductId
                )

            )
            .from(orderEntity)
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .orderBy(orderEntity.id.desc())
            .offset(pageable.getOffset())  // 페이지 시작 위치
            .limit(pageable.getPageSize()) // 페이지 크기
            .fetch();

        return new PageImpl<>(content, pageable, total);

    }

    public GetClientOrderDetailResponseDto getClientOrderDetail(Long orderId,
        MemberAuthEntity member) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(orderEntity.member.eq(member));
        condition.and(orderEntity.id.eq(orderId));

        // 데이터 조회
        GetClientOrderDetailResponseDto result = queryFactory
            .select(new QGetClientOrderDetailResponseDto(
                    orderEntity.id,
                    orderEntity.channelProductOrderId,
                    orderEntity.orderState,
                    orderEntity.ticketState,
                    orderEntity.purchaseAt,
                    orderEntity.product.id,
                    orderEntity.product.productName,
                    orderEntity.product.pri,
                    orderEntity.directOption,
                    orderEntity.purchaseQuantity,
                    orderEntity.purchasePrice,
                    orderEntity.purchaseUserName,
                    orderEntity.phoneNumber,
                    orderEntity.cancelRequestAt,
                    orderEntity.returnRequestAt,
                    orderEntity.cancelOrReturnState,
                    orderEntity.cancelOrReturnReason,
                    orderEntity.cancelOrReturnDetailReason,
                    ticketEntity.ticketKey,
                    ticketEntity.isUsed,
                    paymentEntity.method,
                    paymentEntity.productPrice,
                    paymentEntity.productDiscountRate
                )
            )
            .from(orderEntity)
            .leftJoin(ticketEntity).on(orderEntity.id.eq(ticketEntity.order.id))
            .leftJoin(paymentEntity).on(orderEntity.id.eq(paymentEntity.order.id))
            .where(condition)
            .fetchOne();

        if (result == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }

        return result;
    }

    public Page<GetClientOrderTableResponseDto> getClientProductOrderCancelOrReturnList(
        MemberAuthEntity member,
        Pageable pageable, CancelReturnStateCondition searchDto) {

        CancelReturnStateCondition safeCondition =
            (searchDto != null) ? searchDto : CancelReturnStateCondition.ALL;

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(orderEntity.member.eq(member));
        switch (safeCondition) {
            case ALL: {
                condition.and(orderEntity.cancelOrReturnState.in(
                    CancelOrReturnState.CANCEL_DONE,
                    CancelOrReturnState.CANCELING,
                    CancelOrReturnState.RETURN_DONE
                    
                ));
                break;

            }
            case CANCEL: {
                condition.and(orderEntity.cancelOrReturnState.in(
                    CancelOrReturnState.CANCEL_DONE,
                    CancelOrReturnState.CANCELING
                ));
                break;

            }
            case RETURN: {
                condition.and(orderEntity.cancelOrReturnState.in(
                    CancelOrReturnState.RETURN_DONE
                ));

                break;
            }
        }

        // 총 개수 조회
        Long total = queryFactory
            .select(orderEntity.count())
            .from(orderEntity)
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        PathBuilder<OrderEntity> entityPath = new PathBuilder<>(OrderEntity.class,
            "orderEntity");

        // 데이터 조회
        List<GetClientOrderTableResponseDto> content = queryFactory
            .select(new QGetClientOrderTableResponseDto(
                    orderEntity.id,
                    orderEntity.channelProductOrderId,
                    orderEntity.orderState,
                    orderEntity.cancelOrReturnState,
                    orderEntity.ticketState,
                    orderEntity.purchaseAt,
                    orderEntity.product.id,
                    orderEntity.product.productName,
                    orderEntity.product.pri,
                    orderEntity.directOption,
                    ticketEntity.isUsed

                )

            )
            .from(orderEntity)
            .leftJoin(ticketEntity).on(orderEntity.id.eq(ticketEntity.order.id))
            .where(condition)
            .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
            .offset(pageable.getOffset())  // 페이지 시작 위치
            .limit(pageable.getPageSize()) // 페이지 크기
            .fetch();

        return new PageImpl<>(content, pageable, total);


    }

    public Page<GetClientOrderTableResponseDto> getClientOrderList(
        MemberAuthEntity member,
        Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(orderEntity.member.eq(member));

        // 총 개수 조회
        Long total = queryFactory
            .select(orderEntity.count())
            .from(orderEntity)
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        OrderSpecifier<LocalDateTime> latestDateOrder = new CaseBuilder()
            .when(orderEntity.updateAt.goe(orderEntity.createAt)).then(orderEntity.updateAt)
            .otherwise(orderEntity.createAt)
            .desc();

        // 데이터 조회
        List<GetClientOrderTableResponseDto> content = queryFactory
            .select(new QGetClientOrderTableResponseDto(
                    orderEntity.id,
                    orderEntity.channelProductOrderId,
                    orderEntity.orderState,
                    orderEntity.cancelOrReturnState,
                    orderEntity.ticketState,
                    orderEntity.purchaseAt,
                    orderEntity.product.id,
                    orderEntity.product.productName,
                    orderEntity.product.pri,
                    orderEntity.directOption,
                    ticketEntity.isUsed

                )

            )
            .from(orderEntity)
            .leftJoin(ticketEntity).on(orderEntity.id.eq(ticketEntity.order.id))
            .where(condition)
            .orderBy(latestDateOrder)
            .offset(pageable.getOffset())  // 페이지 시작 위치
            .limit(pageable.getPageSize()) // 페이지 크기
            .fetch();

        return new PageImpl<>(content, pageable, total);


    }

    public Page<GetProductOrderCancelOrReturnRequestTableResponseDto> getProductOrderCancelOrReturnRequestList(
        Pageable pageable, ProductOrdersReturnRequestSearchDto searchDto) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(orderEntity.cancelOrReturnState.in(CancelOrReturnState.CANCEL_REQUEST,
            CancelOrReturnState.RETURN_REQUEST));

        // 날짜 필터링 조건 추가
        if (searchDto.startDate() != null && searchDto.endDate() != null
            && searchDto.dateCriteria() != null) {
            condition.and(_getDateCriteriaCondition(searchDto.dateCriteria(),
                searchDto.startDate(),
                searchDto.endDate()));
        }

        // 상세 검색 조건 추가
        if (searchDto.searchCondition() != null && searchDto.searchKeyword() != null
            && !searchDto.searchKeyword().isEmpty()) {
            switch (searchDto.searchCondition()) {
                case PHONE_NUMBER:
                    condition.and(orderEntity.phoneNumber.contains(searchDto.searchKeyword()));
                    break;
                case USER_NAME:
                    condition.and(orderEntity.purchaseUserName.contains(searchDto.searchKeyword()));
                    break;
                case SOCIAL_ORDER_ID:
                    condition.and(orderEntity.channelOrderId.contains(searchDto.searchKeyword()));
                    break;
                case ORDER_ID:
                    if (NumberUtils.isCreatable(searchDto.searchKeyword())) {
                        Long orderId = Long.valueOf(searchDto.searchKeyword());
                        condition.and(orderEntity.id.eq(orderId));
                    } else {
                        // 숫자가 아니면 조건을 아예 걸지 않음 (전체 리스트를 출력하지 않게)
                        condition.and(orderEntity.id.isNull());
                    }
                    break;
                case PRODUCT_ID:
                    if (NumberUtils.isCreatable(searchDto.searchKeyword())) {
                        Long productId = Long.valueOf(searchDto.searchKeyword());
                        condition.and(productEntity.id.eq(productId));
                    } else {
                        // 숫자가 아니면 조건을 아예 걸지 않음 (전체 리스트를 출력하지 않게)
                        condition.and(productEntity.id.isNull());
                    }
                    break;
            }
        }

        // 총 개수 조회
        Long total = queryFactory
            .select(orderEntity.count())
            .from(orderEntity)
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        // 데이터 조회
        List<GetProductOrderCancelOrReturnRequestTableResponseDto> content = queryFactory
            .select(new QGetProductOrderCancelOrReturnRequestTableResponseDto(
                    orderEntity.id,
                    orderEntity.channel,
                    orderEntity.channelOrderId,
                    orderEntity.channelProductOrderId,
                    orderEntity.orderState,
                    orderEntity.ticketState,
                    orderEntity.purchaseAt,
                    orderEntity.product.id,
                    productEntity.productName,
                    orderEntity.directOption,
                    orderEntity.purchaseQuantity,
                    orderEntity.purchaseUserName,
                    orderEntity.phoneNumber,
                    orderEntity.purchaseUserId,
                    productEntity.channelProductId,
                    orderEntity.cancelRequestAt,
                    orderEntity.returnRequestAt,
                    orderEntity.cancelOrReturnState,
                    orderEntity.cancelOrReturnReason,
                    orderEntity.cancelOrReturnDetailReason,
                    orderEntity.isProgressing
                )

            )
            .from(orderEntity)
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .orderBy(orderEntity.id.desc())
            .offset(pageable.getOffset())  // 페이지 시작 위치
            .limit(pageable.getPageSize()) // 페이지 크기
            .fetch();

        return new PageImpl<>(content, pageable, total);


    }


    public Page<GetProductOrderCancelOrReturnCompleteTableResponseDto> getProductOrderCancelOrReturnCompleteList(
        Pageable pageable, ProductOrdersReturnCompleteSearchDto searchDto) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(orderEntity.cancelOrReturnState.in(CancelOrReturnState.CANCEL_DONE,
            CancelOrReturnState.RETURN_DONE));

        // 날짜 필터링 조건 추가
        if (searchDto.startDate() != null && searchDto.endDate() != null
            && searchDto.dateCriteria() != null) {
            condition.and(_getDateCriteriaCondition(searchDto.dateCriteria(),
                searchDto.startDate(),
                searchDto.endDate()));
        }

        // 상세 검색 조건 추가
        if (searchDto.searchCondition() != null && searchDto.searchKeyword() != null
            && !searchDto.searchKeyword().isEmpty()) {
            switch (searchDto.searchCondition()) {
                case PHONE_NUMBER:
                    condition.and(orderEntity.phoneNumber.contains(searchDto.searchKeyword()));
                    break;
                case USER_NAME:
                    condition.and(orderEntity.purchaseUserName.contains(searchDto.searchKeyword()));
                    break;
                case SOCIAL_ORDER_ID:
                    condition.and(orderEntity.channelOrderId.contains(searchDto.searchKeyword()));
                    break;
                case ORDER_ID:
                    if (NumberUtils.isCreatable(searchDto.searchKeyword())) {
                        Long orderId = Long.valueOf(searchDto.searchKeyword());
                        condition.and(orderEntity.id.eq(orderId));
                    } else {
                        // 숫자가 아니면 조건을 아예 걸지 않음 (전체 리스트를 출력하지 않게)
                        condition.and(orderEntity.id.isNull());
                    }
                    break;
                case PRODUCT_ID:
                    if (NumberUtils.isCreatable(searchDto.searchKeyword())) {
                        Long productId = Long.valueOf(searchDto.searchKeyword());
                        condition.and(productEntity.id.eq(productId));
                    } else {
                        // 숫자가 아니면 조건을 아예 걸지 않음 (전체 리스트를 출력하지 않게)
                        condition.and(productEntity.id.isNull());
                    }
                    break;
            }
        }

        // 총 개수 조회
        Long total = queryFactory
            .select(orderEntity.count())
            .from(orderEntity)
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        // 데이터 조회
        List<GetProductOrderCancelOrReturnCompleteTableResponseDto> content = queryFactory
            .select(new QGetProductOrderCancelOrReturnCompleteTableResponseDto(
                orderEntity.id,
                orderEntity.channel,
                orderEntity.channelOrderId,
                orderEntity.channelProductOrderId,
                orderEntity.orderState,
                orderEntity.purchaseAt,
                orderEntity.product.id,
                productEntity.productName,
                orderEntity.directOption,
                orderEntity.purchaseQuantity,
                orderEntity.purchaseUserName,
                orderEntity.phoneNumber,
                orderEntity.purchaseUserId,
                productEntity.channelProductId,
                orderEntity.cancelRequestAt,
                orderEntity.returnRequestAt,
                orderEntity.cancelDoneAt,
                orderEntity.returnDoneAt,
                orderEntity.cancelOrReturnState,
                orderEntity.cancelOrReturnReason,
                orderEntity.cancelOrReturnDetailReason

            ))
            .from(orderEntity)
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .orderBy(orderEntity.id.desc())
            .offset(pageable.getOffset())  // 페이지 시작 위치
            .limit(pageable.getPageSize()) // 페이지 크기
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }


    public GetProductOrderSummaryDto getProductOrderSummary(Long oderId) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(orderEntity.id.eq(oderId));

        return queryFactory
            .select(new QGetProductOrderSummaryDto(
                orderEntity.id,
                productEntity.productName,
                orderEntity.purchaseUserName,
                orderEntity.phoneNumber,
                orderEntity.directOption

            ))
            .from(orderEntity)
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .fetchOne();
    }

    private BooleanExpression _getDateCriteriaCondition(DateCriteria dateCriteria, LocalDate start,
        LocalDate end) {
        switch (dateCriteria) {
            case PAYMENT_DATE:
                return orderEntity.purchaseAt.between(start.atStartOfDay(), end.atTime(
                    LocalTime.MAX));
            case CANCEL_REQUEST_DATE:
                return orderEntity.cancelRequestAt.between(start.atStartOfDay(),
                    end.atTime(LocalTime.MAX));
            case REFUND_DATE:
                return orderEntity.returnDoneAt.between(start.atStartOfDay(),
                    end.atTime(LocalTime.MAX));
            default:
                throw new IllegalArgumentException("Invalid date criteria: " + dateCriteria);
        }
    }

}
