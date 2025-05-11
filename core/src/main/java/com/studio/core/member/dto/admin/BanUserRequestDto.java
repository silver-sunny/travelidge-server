package com.studio.core.member.dto.admin;

import com.studio.core.global.enums.BanPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record BanUserRequestDto
    (
    @Schema(description = "차단 대상 번호", example = "1") @NotNull
    Long memberNo,
    @Schema(description = "차단 기간", example = "WEEK", allowableValues = {"WEEK", "MONTH", "PERMANENT", "NONE"}) @NotNull
    BanPeriod period
) {}