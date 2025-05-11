package com.studio.api.member.service;

import com.studio.core.global.enums.BanPeriod;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.member.dto.admin.BanUserRequestDto;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.member.repository.MemberAuthJpaRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberAuthJpaRepository memberRepository;

    public void banUser(BanUserRequestDto requestDto) {
        MemberAuthEntity member = memberRepository.findById(requestDto.memberNo())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (requestDto.period() == BanPeriod.NONE) {
            // ✨ 해제 처리

            member.banMember(null,null);
        } else {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime end = requestDto.period().isPermanent() ? null : now.plusDays(requestDto.period().getDays());

            member.banMember(now,end);

        }

        memberRepository.save(member);
    }

}
