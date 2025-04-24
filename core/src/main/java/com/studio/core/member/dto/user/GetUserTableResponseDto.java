package com.studio.core.member.dto.user;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.AuthRole;
import com.studio.core.global.enums.ProviderType;
import lombok.Getter;

@Getter
public class GetUserTableResponseDto {

    private final Long memberNo;
    private final ProviderType providerType;
    private final String phoneNumber;
    private final String name;
    private final String nickname;
    private final AuthRole role;

    @QueryProjection
    public GetUserTableResponseDto(Long memberNo, ProviderType providerType, String phoneNumber,
        String name, String nickname, AuthRole role) {
        this.memberNo = memberNo;
        this.providerType = providerType;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.nickname = nickname;
        this.role = role;
    }


    public String getRoleName() {
        return role.getMeaning();
    }
}

