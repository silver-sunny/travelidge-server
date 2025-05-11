package com.studio.core.report.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.ReportTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "신고 응답 DTO")
public record GetClientReportTableResponse(

        @Schema(description = "신고 대상 타입")
        ReportTargetType targetType,

        @Schema(description = "신고 대상 번호")
        Long targetNumber,

        @Schema(description = "신고 사유")
        String reason,

        @Schema(description = "신고 일시")
        LocalDateTime reportedAt

    ) {
        @QueryProjection
        public GetClientReportTableResponse {}
    }
