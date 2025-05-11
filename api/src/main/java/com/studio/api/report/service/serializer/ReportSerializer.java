package com.studio.api.report.service.serializer;

import com.studio.core.global.enums.ReportTargetType;
import com.studio.core.inquiry.entity.InquiryEntity;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.report.dto.ReportRequestDto;
import com.studio.core.report.entity.ReportEntity;

public class ReportSerializer {
    public static ReportEntity toReportEntity(
        ReportRequestDto dto,
        MemberAuthEntity reporter,
        MemberAuthEntity targetMember,
        InquiryEntity targetInquiry
    ) {
        return ReportEntity.builder()
            .reporter(reporter)
            .target(targetMember)
            .inquiry(targetInquiry)
            .reason(dto.reason())
            .targetType(dto.targetType())
            .build();
    }

}
