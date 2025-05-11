package com.studio.core.report.dto;

import com.studio.core.global.enums.ReportTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "신고 요청 DTO")
public record ReportRequestDto(

    @Schema(description = "신고 대상 타입", example = "USER")
    @NotNull
    ReportTargetType targetType,

    @Schema(description = "신고 대상 번호", example = "1")
    @NotNull
    Long targetNumber,

    @Schema(description = "신고 내용", example = "부적절한 언행을 사용함.")
    @NotBlank
    @Size(min = 10, max = 300)
    String reason

) {}

