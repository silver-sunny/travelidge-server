package com.studio.core.member.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangeAuthDto(
        @NotNull
        @Size(min = 4, max = 12)
        @Pattern(regexp = "^[a-zA-Z0-9]{1,14}$", message =  "비밀번호는 영어 및 숫자로만 구성되어야 합니다.")
        @Schema(description = "변경할 비밀번호", example = "1111") String password) {
}
