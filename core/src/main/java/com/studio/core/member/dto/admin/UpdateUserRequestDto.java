package com.studio.core.member.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
        @Size(min = 2, max = 10)
        @Schema(description = "변경할 이름", example = "김땡땡") String name,

        @Size(min = 2, max = 14)
        @Schema(description = "변경할 닉네임", example = "김땡땡") String nickname,

        @Schema(description = "변경할 핸드폰 번호", example = "01012341234") String phone
        ) {
}
