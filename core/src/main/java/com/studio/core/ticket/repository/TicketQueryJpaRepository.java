package com.studio.core.ticket.repository;


import com.querydsl.core.BooleanBuilder;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.studio.core.order.entity.QOrderEntity;
import com.studio.core.product.entity.QProductEntity;
import com.studio.core.ticket.dto.AllTicketSearchDto;
import com.studio.core.ticket.dto.GetTicketTableResponseDto;
import com.studio.core.ticket.dto.QGetTicketTableResponseDto;
import com.studio.core.ticket.entity.QTicketEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class TicketQueryJpaRepository {

    private final JPAQueryFactory queryFactory;

    private final QTicketEntity ticketEntity = QTicketEntity.ticketEntity;
    private static final QOrderEntity orderEntity = QOrderEntity.orderEntity;
    private static final QProductEntity productEntity = QProductEntity.productEntity;

    public Page<GetTicketTableResponseDto> getTicketList(Pageable pageable,
        AllTicketSearchDto searchDto) {

        BooleanBuilder condition = new BooleanBuilder();

        if (searchDto.searchCondition() != null && searchDto.searchKeyword() != null
            && !searchDto.searchKeyword().isEmpty()) {
            switch (searchDto.searchCondition()) {
                case PHONE_NUMBER:
                    condition.and(orderEntity.phoneNumber.contains(
                        searchDto.searchKeyword().replace("-", "")));
                    break;
                case TICKET_KEY:
                    condition.and(ticketEntity.ticketKey.contains(searchDto.searchKeyword()));
                    break;
                case SOCIAL_ORDER_ID:
                    condition.and(
                        orderEntity.channelOrderId.contains(searchDto.searchKeyword()));
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
            }
        }

        // 총 개수 조회
        Long total = queryFactory
            .select(orderEntity.count())
            .from(ticketEntity)
            .join(orderEntity).on(ticketEntity.order.id.eq(orderEntity.id))
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        // 데이터 조회
        List<GetTicketTableResponseDto> content = queryFactory
            .select(new QGetTicketTableResponseDto(
                    ticketEntity.ticketKey,
                    ticketEntity.isUsed,
                    ticketEntity.usedAt,

                    orderEntity.id,
                    orderEntity.channel,
                    orderEntity.channelOrderId,
                    orderEntity.channelProductOrderId,

                    orderEntity.purchaseUserName,
                    orderEntity.phoneNumber,
                    orderEntity.orderState,
                    orderEntity.cancelOrReturnState,
                    orderEntity.purchaseAt,
                    orderEntity.purchaseQuantity,
                    orderEntity.directOption,
                    orderEntity.product.id,

                    productEntity.productName,
                    productEntity.channelProductId

                )

            )
            .from(ticketEntity)
            .join(orderEntity).on(ticketEntity.order.id.eq(orderEntity.id))
            .join(productEntity).on(orderEntity.product.id.eq(productEntity.id))
            .where(condition)
            .orderBy(ticketEntity.createAt.desc())
            .offset(pageable.getOffset())  // 페이지 시작 위치
            .limit(pageable.getPageSize()) // 페이지 크기
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }

}
