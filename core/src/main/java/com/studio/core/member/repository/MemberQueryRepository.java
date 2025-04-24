package com.studio.core.member.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studio.core.member.dto.admin.GetAdminTableResponseDto;
import com.studio.core.global.enums.AuthRole;
import com.studio.core.global.utils.QueryDslUtil;
import com.studio.core.member.dto.admin.QGetAdminTableResponseDto;
import com.studio.core.member.dto.user.GetUserTableResponseDto;
import com.studio.core.member.dto.user.QGetUserTableResponseDto;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.member.entity.QMemberAuthEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    private static final QMemberAuthEntity memberAuthEntity = QMemberAuthEntity.memberAuthEntity;


    public Page<GetAdminTableResponseDto> findAllExcludingRoleRoot(Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(
            memberAuthEntity.role.eq(AuthRole.ROLE_MANAGE)
                .or(memberAuthEntity.role.eq(AuthRole.ROLE_MANAGE))
        );

        int total = queryFactory
            .select(memberAuthEntity)
            .from(memberAuthEntity)
            .where(condition)
            .fetch()
            .size();
        if (total == 0) {
            return Page.empty(pageable);
        }
        PathBuilder<MemberAuthEntity> entityPath = new PathBuilder<>(MemberAuthEntity.class,
            "memberAuthEntity");

        List<GetAdminTableResponseDto> content = queryFactory
            .select(new QGetAdminTableResponseDto(
                memberAuthEntity.memberNo,
                memberAuthEntity.id,
                memberAuthEntity.role
            ))
            .from(memberAuthEntity)
            .where(condition)
            .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }


    public Page<GetUserTableResponseDto> getUsers(Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(
            memberAuthEntity.role.eq(AuthRole.ROLE_USER)
        );

        int total = queryFactory
            .select(memberAuthEntity)
            .from(memberAuthEntity)
            .where(condition)
            .fetch()
            .size();
        if (total == 0) {
            return Page.empty(pageable);
        }
        PathBuilder<MemberAuthEntity> entityPath = new PathBuilder<>(MemberAuthEntity.class,
            "memberAuthEntity");

        List<GetUserTableResponseDto> content = queryFactory
            .select(new QGetUserTableResponseDto(
                memberAuthEntity.memberNo,
                memberAuthEntity.providerType,
                memberAuthEntity.phoneNumber,
                memberAuthEntity.name,
                memberAuthEntity.nickname,
                memberAuthEntity.role
            ))
            .from(memberAuthEntity)
            .where(condition)
            .orderBy(QueryDslUtil.getOrderSpecifiers(pageable.getSort(), entityPath))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
