package com.studio.core.inquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductInquiryRequestDto(
        @Schema(description = "문의 내용", example = "문의합니다.~~")
        @Size(min = 10, max = 100)
        @NotBlank
        String inquiry,

        @Schema(description = "비밀글 여부")
        @NotNull
        boolean isPrivate
) {
}
