package com.studio.api.global.config.security;

import com.studio.core.global.exception.CustomException;
import com.studio.core.member.entity.MemberAuthEntity;

import com.studio.core.member.repository.MemberAuthJpaRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.studio.core.global.exception.ErrorCode.BANNED_USER;
import static com.studio.core.global.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomAdminDetailService implements UserDetailsService {

    private final MemberAuthJpaRepository memberAuthRepository;

    @Override
    public MemberPrincipal loadUserByUsername(String memberNo) throws UsernameNotFoundException {

        MemberAuthEntity memberAuth = memberAuthRepository.findByMemberNo(Long.valueOf(memberNo))
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return new MemberPrincipal(memberAuth);

    }
}
