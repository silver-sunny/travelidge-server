package com.studio.api.global.service;

import com.studio.api.global.config.security.MemberPrincipal;
import com.studio.core.global.enums.AuthRole;
import com.studio.core.member.entity.MemberAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonService {

    public static MemberAuthEntity getMemberFromAuth(Authentication authentication) {
        return ((MemberPrincipal) authentication.getPrincipal()).getMember();
    }

    public static boolean isAdmin(Authentication authentication) {
        MemberAuthEntity member = ((MemberPrincipal) authentication.getPrincipal()).getMember();
        return !AuthRole.ROLE_USER.equals(member.getRole());
    }

    public static boolean isAdmin(MemberAuthEntity member) {
        return !AuthRole.ROLE_USER.equals(member.getRole());
    }
}
