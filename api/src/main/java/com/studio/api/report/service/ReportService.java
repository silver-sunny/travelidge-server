package com.studio.api.report.service;


import com.studio.api.report.service.serializer.ReportSerializer;
import com.studio.core.global.enums.ReportTargetType;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.inquiry.entity.InquiryEntity;
import com.studio.core.inquiry.repository.InquiryJpaRepository;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.member.repository.MemberAuthJpaRepository;
import com.studio.core.report.dto.ReportRequestDto;
import com.studio.core.report.entity.ReportEntity;
import com.studio.core.report.repository.ReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportJpaRepository reportJpaRepository;
    private final InquiryJpaRepository inquiryRepository;
    private final MemberAuthJpaRepository memberRepository;


    public void insertReport(ReportRequestDto dto, MemberAuthEntity reporter) {
        MemberAuthEntity targetUser = null;
        InquiryEntity targetInquiry = null;

        ReportTargetType type = dto.targetType();
        if (type == ReportTargetType.USER) {
            targetUser = memberRepository.findById(dto.targetNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        } else if (type == ReportTargetType.REVIEW) {
            targetInquiry = inquiryRepository.findById(dto.targetNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));
            targetUser = targetInquiry.getMember();
        }

        ReportEntity report = ReportSerializer.toReportEntity(dto, reporter, targetUser, targetInquiry);
        reportJpaRepository.save(report);
    }
}

