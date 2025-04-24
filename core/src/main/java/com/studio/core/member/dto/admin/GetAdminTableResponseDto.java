package com.studio.core.member.dto.admin;

import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.AuthRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "운영자 리스트")
public class GetAdminTableResponseDto {

    private final Long memberNo;
    private final String id;
    private final AuthRole role;

    @QueryProjection
    public GetAdminTableResponseDto(Long memberNo, String id, AuthRole role) {
        this.memberNo = memberNo;
        this.id = id;
        this.role = role;
    }

    public String getRoleName() {
        return role.getMeaning();
    }
}

