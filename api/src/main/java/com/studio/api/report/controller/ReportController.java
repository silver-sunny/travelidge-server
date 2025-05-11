package com.studio.api.report.controller;

import com.studio.core.global.response.SuccessResponse;
import com.studio.core.report.dto.GetReportTableResponse;
import com.studio.core.report.repository.ReportQueryJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/report")
@RequiredArgsConstructor
@Tag(name = "Report Admin Controller", description = "관리자 신고목록")
public class ReportController {
    private final ReportQueryJpaRepository reportQueryJpaRepository;

    @Operation(summary = "전체 신고 목록 조회")
    @GetMapping("")
    public SuccessResponse<Page<GetReportTableResponse>> getAllReports(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reportedAt"));
        Page<GetReportTableResponse> result = reportQueryJpaRepository.getAdminReports(pageable);
        return SuccessResponse.ok(result);
    }

}
