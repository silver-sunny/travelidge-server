package com.studio.core.report.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studio.core.global.enums.ReportTargetType;
import com.studio.core.report.dto.GetClientReportTableResponse;
import com.studio.core.report.dto.GetReportTableResponse;
import com.studio.core.report.dto.QGetClientReportTableResponse;
import com.studio.core.report.dto.QGetReportTableResponse;
import com.studio.core.report.entity.QReportEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportQueryJpaRepository {

    private final JPAQueryFactory queryFactory;
    private static final QReportEntity report = QReportEntity.reportEntity;

    public List<GetClientReportTableResponse> getClientReports(Long memberNo) {
        return queryFactory
            .select(new QGetClientReportTableResponse(
                report.targetType,
                report.inquiry.id.coalesce(report.target.memberNo),
                report.reason,
                report.reportedAt
            ))
            .from(report)
            .where(report.reporter.memberNo.eq(memberNo))
            .orderBy(report.reportedAt.desc())
            .fetch();
    }

//    public List<GetReportTableResponse> getAdminReports(Pageable pageable) {
//        return queryFactory
//            .select(new QGetReportTableResponse(
//                report.id,
//                report.reporter.memberNo,
//                report.targetType,
//                report.inquiry.id.coalesce(report.target.memberNo),
//                report.reason,
//                report.reportedAt
//            ))
//            .from(report)
//            .orderBy(report.reportedAt.desc())
//            .fetch();
//    }

    public Page<GetReportTableResponse> getAdminReports(Pageable pageable) {
        List<GetReportTableResponse> content = queryFactory
            .select(new QGetReportTableResponse(
                report.id,
                report.reporter.memberNo,
                report.targetType,
                report.inquiry.id.coalesce(report.target.memberNo),
                report.reason,
                report.reportedAt
            ))
            .from(report)
            .orderBy(report.reportedAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(report.count())
            .from(report)
            .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}