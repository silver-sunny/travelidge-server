package com.studio.core.inquiry.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studio.core.global.enums.inquiry.search.FilterClientInquiry;
import com.studio.core.global.enums.inquiry.InquiryPrivateState;
import com.studio.core.global.enums.inquiry.InquiryResolvedState;
import com.studio.core.global.utils.QueryDslUtil;
import com.studio.core.inquiry.dto.GetClientProductInquiryTableResponse;
import com.studio.core.inquiry.dto.GetInquiryTableResponse;
import com.studio.core.inquiry.dto.GetProductInquiryTableResponse;
import com.studio.core.inquiry.dto.QGetInquiryTableResponse;
import com.studio.core.inquiry.dto.search.InquirySearchDto;
import com.studio.core.inquiry.dto.search.ProductInquirySearchDto;
import com.studio.core.inquiry.dto.QGetClientProductInquiryTableResponse;
import com.studio.core.inquiry.dto.QGetProductInquiryTableResponse;
import com.studio.core.inquiry.entity.InquiryEntity;
import com.studio.core.inquiry.entity.QInquiryEntity;
import com.studio.core.member.entity.QMemberAuthEntity;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InquiryQueryJpaRepository {

    private final JPAQueryFactory queryFactory;


    private static final QMemberAuthEntity memberAuthEntity = QMemberAuthEntity.memberAuthEntity;
    private static final QInquiryEntity inquiryEntity = QInquiryEntity.inquiryEntity;

    public Page<GetProductInquiryTableResponse> getMyProductInquiries(Long memberNo,
        Pageable pageable) {
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(inquiryEntity.product.isNotNull());
        condition.and(inquiryEntity.member.memberNo.eq(memberNo));

        Long total = queryFactory
            .select(inquiryEntity.count())
            .from(inquiryEntity)
            .leftJoin(inquiryEntity.member, memberAuthEntity)
            .where(condition)
            .fetchOne();
        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        PathBuilder<InquiryEntity> entityPath = new PathBuilder<>(InquiryEntity.class,
            "inquiryEntity");
        List<GetProductInquiryTableResponse> content = queryFactory
            .select(new QGetProductInquiryTableResponse(
                inquiryEntity.id,
                inquiryEntity.product.channel,
                inquiryEntity.inquiry,
                inquiryEntity.answer,
                inquiryEntity.inquiryAt,
                inquiryEntity.answerAt,
                inquiryEntity.product.id,
                inquiryEntity.product.productName,
                inquiryEntity.isResolved,
                memberAuthEntity.memberNo,
                memberAuthEntity.nickname
            ))
            .from(inquiryEntity)
            .join(inquiryEntity.member, memberAuthEntity)
            .where(condition)
            .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);


    }

    public Page<GetProductInquiryTableResponse> getProductInquiries(
        ProductInquirySearchDto searchDto,
        Pageable pageable) {
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(inquiryEntity.product.isNotNull());

        // 질문한 날짜 검색
        if (searchDto.startDate() != null && searchDto.endDate() != null) {
            condition.and(inquiryEntity.inquiryAt.between(searchDto.startDate().atStartOfDay(),
                searchDto.endDate().atTime(
                    LocalTime.MAX)));
        }

        if (searchDto.filterInquiry() != null) {
            switch (searchDto.filterInquiry()) {
                case NOT_RESOLVED: {
                    condition.and(inquiryEntity.isResolved.eq(InquiryResolvedState.NOT_RESOLVED));
                    break;
                }
                case RESOLVED: {
                    condition.and(inquiryEntity.isResolved.eq(InquiryResolvedState.RESOLVED));
                    break;

                }
            }
        }

        if (searchDto.searchCondition() != null && searchDto.searchKeyword() != null
            && !searchDto.searchKeyword().isEmpty()) {
            switch (searchDto.searchCondition()) {
                case PRODUCT_ID:
                    if (NumberUtils.isCreatable(searchDto.searchKeyword())) {
                        Long productId = Long.valueOf(searchDto.searchKeyword());
                        condition.and(inquiryEntity.product.id.eq(productId));

                    }
                    break;
                case PRODUCT_NAME:
                    condition.and(
                        inquiryEntity.product.productName.contains(searchDto.searchKeyword()));
            }

        }
        Long total = queryFactory
            .select(inquiryEntity.count())
            .from(inquiryEntity)
            .leftJoin(inquiryEntity.member, memberAuthEntity)
            .where(condition)
            .fetchOne();
        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        PathBuilder<InquiryEntity> entityPath = new PathBuilder<>(InquiryEntity.class,
            "inquiryEntity");
        List<GetProductInquiryTableResponse> content = queryFactory
            .select(new QGetProductInquiryTableResponse(
                inquiryEntity.id,
                inquiryEntity.product.channel,
                inquiryEntity.inquiry,
                inquiryEntity.answer,
                inquiryEntity.inquiryAt,
                inquiryEntity.answerAt,
                inquiryEntity.product.id,
                inquiryEntity.product.productName,
                inquiryEntity.isResolved,
                memberAuthEntity.memberNo,
                memberAuthEntity.nickname
            ))
            .from(inquiryEntity)
            .leftJoin(inquiryEntity.member, memberAuthEntity)
            .where(condition)
            .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);


    }


    public Page<GetInquiryTableResponse> getInquiries(
        InquirySearchDto searchDto,
        Pageable pageable) {
        BooleanBuilder condition = new BooleanBuilder();

        condition.and(inquiryEntity.product.isNull()); // product가 null인 경우만 조회

        // 질문한 날짜 검색
        if (searchDto.startDate() != null && searchDto.endDate() != null) {
            condition.and(inquiryEntity.inquiryAt.between(searchDto.startDate().atStartOfDay(),
                searchDto.endDate().atTime(
                    LocalTime.MAX)));
        }

        if (searchDto.filterInquiry() != null) {
            switch (searchDto.filterInquiry()) {
                case NOT_RESOLVED: {
                    condition.and(inquiryEntity.isResolved.eq(InquiryResolvedState.NOT_RESOLVED));
                    break;
                }
                case RESOLVED: {
                    condition.and(inquiryEntity.isResolved.eq(InquiryResolvedState.RESOLVED));
                    break;

                }
            }
        }

        Long total = queryFactory
            .select(inquiryEntity.count())
            .from(inquiryEntity)
            .leftJoin(inquiryEntity.member, memberAuthEntity)
            .where(condition)
            .fetchOne();
        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        PathBuilder<InquiryEntity> entityPath = new PathBuilder<>(InquiryEntity.class,
            "inquiryEntity");
        List<GetInquiryTableResponse> content = queryFactory
            .select(new QGetInquiryTableResponse(
                inquiryEntity.id,
                inquiryEntity.inquiry,
                inquiryEntity.answer,
                inquiryEntity.inquiryAt,
                inquiryEntity.answerAt,
                inquiryEntity.isResolved,
                memberAuthEntity.memberNo,
                memberAuthEntity.nickname
            ))
            .from(inquiryEntity)
            .leftJoin(inquiryEntity.member, memberAuthEntity)
            .where(condition)
            .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);


    }

    public Page<GetClientProductInquiryTableResponse> getClientProductInquiries(Long productId,
        FilterClientInquiry filterClientInquiry, Long memberNo, Pageable pageable) {
        if (memberNo == null) {
            return getNotLoginProductInquiries(productId, filterClientInquiry, pageable);
        } else {
            return getLoginProductInquiries(productId, filterClientInquiry, memberNo, pageable);
        }
    }


    private Page<GetClientProductInquiryTableResponse> getLoginProductInquiries(Long productId,
        FilterClientInquiry filterClientInquiry, Long memberNo, Pageable pageable) {
        BooleanBuilder condition = createConditionForProductInquiry(productId, filterClientInquiry,
            memberNo);
        Long total = countProductInquiries(condition);

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        List<GetClientProductInquiryTableResponse> content = fetchProductInquiries(condition,
            pageable,
            memberNo);
        return new PageImpl<>(content, pageable, total);
    }

    private Page<GetClientProductInquiryTableResponse> getNotLoginProductInquiries(Long productId,
        FilterClientInquiry filterClientInquiry, Pageable pageable) {
        if (productId == null) {
            return Page.empty(pageable);
        }

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(inquiryEntity.product.id.eq(productId));

        switch (Optional.ofNullable(filterClientInquiry).orElse(FilterClientInquiry.ALL)) {
            case RESOLVED ->
                condition.and(inquiryEntity.isResolved.eq(InquiryResolvedState.RESOLVED));
            case NOT_RESOLVED ->
                condition.and(inquiryEntity.isResolved.eq(InquiryResolvedState.NOT_RESOLVED));
            case MY_INQUIRY -> {
                return Page.empty(pageable);
            }
        }

        Long total = countProductInquiries(condition);

        if (total == null || total == 0) {
            return Page.empty(pageable);
        }

        List<GetClientProductInquiryTableResponse> content = fetchProductInquiries(condition,
            pageable,
            null);
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanBuilder createConditionForProductInquiry(Long productId,
        FilterClientInquiry filterClientInquiry, Long memberNo) {
        BooleanBuilder condition = new BooleanBuilder();

        if (productId != null) {
            condition.and(inquiryEntity.product.id.eq(productId));
        } else {
            condition.and(inquiryEntity.product.id.isNull());
        }

        switch (Optional.ofNullable(filterClientInquiry).orElse(FilterClientInquiry.ALL)) {
            case RESOLVED ->
                condition.and(inquiryEntity.isResolved.eq(InquiryResolvedState.RESOLVED));
            case NOT_RESOLVED ->
                condition.and(inquiryEntity.isResolved.eq(InquiryResolvedState.NOT_RESOLVED));
            case MY_INQUIRY -> condition.and(inquiryEntity.member.memberNo.eq(memberNo));
        }

        return condition;
    }

    private Long countProductInquiries(BooleanBuilder condition) {
        return queryFactory
            .select(inquiryEntity.count())
            .from(inquiryEntity)
            .leftJoin(inquiryEntity.member, memberAuthEntity)
            .where(condition)
            .fetchOne();
    }

    private List<GetClientProductInquiryTableResponse> fetchProductInquiries(
        BooleanBuilder condition,
        Pageable pageable, Long memberNo) {

        PathBuilder<InquiryEntity> entityPath = new PathBuilder<>(InquiryEntity.class,
            "inquiryEntity");

        BooleanExpression privateCondition = inquiryEntity.isPrivate.eq(InquiryPrivateState.PRIVATE)
            .and(memberNo == null ? Expressions.TRUE : inquiryEntity.member.memberNo.ne(memberNo));

        return queryFactory
            .select(new QGetClientProductInquiryTableResponse(
                inquiryEntity.id,
                Expressions.cases()
                    .when(privateCondition)
                    .then("비공개")
                    .otherwise(inquiryEntity.inquiry),
                Expressions.cases()
                    .when(privateCondition)
                    .then("비공개")
                    .otherwise(inquiryEntity.answer),
                inquiryEntity.inquiryAt,
                inquiryEntity.answerAt,
                inquiryEntity.product.id,
                inquiryEntity.isPrivate,
                inquiryEntity.isResolved,
                inquiryEntity.member.memberNo,
                Expressions.cases()
                    .when(privateCondition)
                    .then("비공개")
                    .otherwise(inquiryEntity.member.nickname)
            ))
            .from(inquiryEntity)
            .where(condition)
            .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }
}
