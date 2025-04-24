package com.studio.core.review.repository;


import static com.querydsl.core.group.GroupBy.groupBy;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.dto.GetClientOrderTableResponseDto;
import com.studio.core.order.dto.QGetClientOrderTableResponseDto;
import com.studio.core.order.entity.QOrderEntity;
import com.studio.core.review.dto.GetClientMyProductReviewTableResponseDto;
import com.studio.core.review.dto.GetClientProductReviewTableResponseDto;
import com.studio.core.review.dto.QGetClientMyProductReviewTableResponseDto;
import com.studio.core.review.dto.QGetClientProductReviewTableResponseDto;
import com.studio.core.review.entity.QReviewEntity;
import com.studio.core.review.entity.QReviewImageEntity;
import com.studio.core.ticket.entity.QTicketEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ReviewQueryJpaRepository {

    private final JPAQueryFactory queryFactory;


    private static final QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
    private static final QReviewImageEntity reviewImageEntity = QReviewImageEntity.reviewImageEntity;

    private static final QOrderEntity orderEntity = QOrderEntity.orderEntity;
    private static final QTicketEntity ticketEntity = QTicketEntity.ticketEntity;

    public Page<GetClientProductReviewTableResponseDto> getClientProductReviews(Long productId,
        Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(reviewEntity.product.id.eq(productId));
        Long total = queryFactory
            .select(reviewEntity.count())
            .from(reviewEntity)
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        List<Long> reviewIds = queryFactory
            .select(reviewEntity.id)
            .from(reviewEntity)
            .where(condition)
            .orderBy(reviewEntity.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<GetClientProductReviewTableResponseDto> content = queryFactory
            .from(reviewEntity)
            .leftJoin(reviewImageEntity).on(reviewEntity.id.eq(reviewImageEntity.review.id))

            .where(reviewEntity.id.in(reviewIds))
            .orderBy(reviewEntity.id.desc())
            .transform(groupBy(reviewEntity.id)
                .list(new QGetClientProductReviewTableResponseDto(
                    reviewEntity.id,
                    reviewEntity.content,
                    reviewEntity.rating,
                    GroupBy.list(
                        reviewImageEntity.imageUrl
                    ),
                    reviewEntity.createAt,
                    reviewEntity.member.nickname,
                    reviewEntity.member.memberNo
                ))
            );



        return new PageImpl<>(content, pageable, total);
    }


    public Page<GetClientMyProductReviewTableResponseDto> getClientMyProductReviews(
        MemberAuthEntity member,
        Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(reviewEntity.member.eq(member));
        Long total = queryFactory
            .select(reviewEntity.count())
            .from(reviewEntity)
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        List<Long> reviewIds = queryFactory
            .select(reviewEntity.id)
            .from(reviewEntity)
            .where(condition)
            .orderBy(reviewEntity.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<GetClientMyProductReviewTableResponseDto> content = queryFactory
            .from(reviewEntity)
            .leftJoin(reviewImageEntity).on(reviewEntity.id.eq(reviewImageEntity.review.id))
            .where(reviewEntity.id.in(reviewIds))
            .orderBy(reviewEntity.id.desc())
            .transform(groupBy(reviewEntity.id)
                .list(new QGetClientMyProductReviewTableResponseDto(
                    reviewEntity.id,
                    reviewEntity.content,
                    reviewEntity.rating,
                    GroupBy.list(
                        reviewImageEntity.imageUrl
                    ),
                    reviewEntity.createAt,
                    reviewEntity.product.id,
                    reviewEntity.product.productName,
                    reviewEntity.product.pri,
                    reviewEntity.order.directOption
                ))
            );

//
//        List<GetClientMyProductReviewTableResponseDto> content =
//            queryFactory.from(reviewEntity)
//                .leftJoin(reviewImageEntity).on(reviewEntity.id.eq(reviewImageEntity.review.id))
//                .where(condition)
//                .orderBy(reviewEntity.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .transform(
//                    groupBy(reviewEntity).list(new QGetClientMyProductReviewTableResponseDto(
//                            reviewEntity.id,
//                            reviewEntity.content,
//                            reviewEntity.rating,
//                            GroupBy.list(
//                                reviewImageEntity.imageUrl
//                            ),
//                            reviewEntity.createAt,
//                        reviewEntity.product.id,
//                        reviewEntity.product.productName,
//                        reviewEntity.product.pri,
//                        reviewEntity.order.directOption
//
//                        )
//                    ));
        return new PageImpl<>(content, pageable, total);
    }


    public Page<GetClientOrderTableResponseDto> getMyUnwrittenProductReviewList(
        MemberAuthEntity member,
        Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(orderEntity.member.eq(member));
         condition.and(ticketEntity.isUsed.eq(TicketUsedState.USED));
        condition.and(reviewEntity.id.isNull()); // 리뷰가 없는 주문만 필터링

        // 총 개수 조회
        Long total = queryFactory
            .select(orderEntity.count())
            .from(orderEntity)
            .join(ticketEntity).on(orderEntity.id.eq(ticketEntity.order.id))
            .leftJoin(reviewEntity).on(orderEntity.id.eq(reviewEntity.order.id))
            .where(condition)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

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
            .join(ticketEntity).on(orderEntity.id.eq(ticketEntity.order.id))
            .leftJoin(reviewEntity).on(orderEntity.id.eq(reviewEntity.order.id))
            .where(condition)
            .orderBy(orderEntity.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }


}
