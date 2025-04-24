package com.studio.core.member.dto.admin;

import com.studio.core.global.enums.ProviderType;
import com.studio.core.member.entity.MemberAuthEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record MyUserInfoResponseDto(
    @Schema(description = "변경할 이름") String name,
    @Schema(description = "닉네임") String nickname,
    @Schema(description = "핸드폰 번호") String phone,
    ProviderType providerType,
    String email
) {
    public MyUserInfoResponseDto(MemberAuthEntity memberAuth) {
        this(
            memberAuth.getName(),
            memberAuth.getNickname(),
            memberAuth.getPhoneNumber(),
            memberAuth.getProviderType(),
            memberAuth.getEmail()
        );
    }
}

