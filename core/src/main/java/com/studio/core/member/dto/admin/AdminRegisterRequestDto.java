package com.studio.core.member.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AdminRegisterRequestDto(@NotNull
                                      @Schema(description = "관리자 ID", example = "admin1")String id) {
}
