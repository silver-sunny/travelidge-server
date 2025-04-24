package com.studio.core.inquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InquiryRequestDto(
        @Schema(description = "문의 내용",example = "문의합니다.~~")
        @Size(min = 10, max = 300)
        @NotBlank
        String inquiry
) {
}
