package com.studio.api.report.controller;

import static com.studio.api.global.service.CommonService.getMemberFromAuth;

import com.studio.api.report.service.ReportService;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.report.dto.ReportRequestDto;
import com.studio.core.report.repository.ReportQueryJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/api/client/report")
@RequiredArgsConstructor
@Tag(name = "Report Client Controller", description = "클라이언트 신고목록")
public class ReportClientController {
    private final ReportService reportService;
    private final ReportQueryJpaRepository reportQueryJpaRepository;

    @Operation(summary = "신고하기")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    public SuccessResponse<Void> insertReport(
        @Validated @RequestBody ReportRequestDto requestDto,
        Authentication authentication
    ) {
        MemberAuthEntity reporter = getMemberFromAuth(authentication);

        reportService.insertReport(requestDto, reporter);

        return SuccessResponse.ok();
    }

    @Operation(summary = "나의 신고내역 리스트")
    @GetMapping("/product")
    public SuccessResponse<?> getMyReports(Authentication authentication) {

        MemberAuthEntity member = getMemberFromAuth(authentication);

        return SuccessResponse.ok(reportQueryJpaRepository.getClientReports(member.getMemberNo()));
    }

}
