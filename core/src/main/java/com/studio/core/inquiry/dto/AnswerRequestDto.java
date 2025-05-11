package com.studio.core.inquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnswerRequestDto(
        @Schema(description = "문의 답변",example = "답변합니다.~~")
        @Size(min = 10, max = 300)
        @NotBlank
        String answer
) {
}
