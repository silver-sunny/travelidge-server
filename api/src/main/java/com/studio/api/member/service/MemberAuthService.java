package com.studio.api.member.service;


import com.studio.api.global.config.security.AuthToken;
import com.studio.api.global.config.security.AuthTokenProvider;
import com.studio.api.global.utils.CryptoUtil;
import com.studio.core.member.dto.admin.AdminRegisterRequestDto;
import com.studio.core.member.dto.admin.ChangeAuthDto;
import com.studio.core.member.dto.admin.LoginRequestDto;
import com.studio.core.global.enums.AuthRole;
import com.studio.core.global.exception.CustomException;
import com.studio.core.member.dto.admin.UpdateUserRequestDto;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.member.entity.RefreshTokenEntity;
import com.studio.core.member.repository.MemberAuthJpaRepository;
import com.studio.core.member.repository.RefreshTokenJpaRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import static com.studio.core.global.exception.ErrorCode.ADMIN_ALREADY_ID;
import static com.studio.core.global.exception.ErrorCode.ADMIN_PASSWORD_MISMATCH;
import static com.studio.core.global.exception.ErrorCode.ALREADY_NICKNAME;
import static com.studio.core.global.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.NOT_ADMIN_OR_AVAILABLE;


@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private final MemberAuthJpaRepository memberAuthRepository;
    private final AuthTokenProvider authTokenProvider;
    private final RefreshTokenJpaRepository refreshTokenRepository;

    private final String DEFAULT_PASSWORD = "1111";

    public void registerAdminAuth(AdminRegisterRequestDto adminRegisterRequestDto) {
        if (memberAuthRepository.existsById(adminRegisterRequestDto.id())) {
            throw new CustomException(ADMIN_ALREADY_ID);
        }

        MemberAuthEntity memberAuth =
            MemberAuthEntity.builder()
                .id(adminRegisterRequestDto.id())
                .password(CryptoUtil.encrypt(DEFAULT_PASSWORD))
                .role(AuthRole.ROLE_MANAGE)
                .build();

        memberAuthRepository.save(memberAuth);

    }

    public boolean existsById(String id) {
        return memberAuthRepository.existsById(id);
    }

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        MemberAuthEntity existMemberAuth = memberAuthRepository.findById(loginRequestDto.id())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        // 비밀번호 일치하는지 확인
        if (!CryptoUtil.isPasswordMatch(loginRequestDto.password(), existMemberAuth.getPassword()
        )) {
            throw new CustomException(ADMIN_PASSWORD_MISMATCH);
        }
        AuthToken accessToken = authTokenProvider.createAccessToken(existMemberAuth.getMemberNo(),
            existMemberAuth.getId(), existMemberAuth.getRole());

        // 리프레시 토큰 저장
        RefreshTokenEntity refreshToken = refreshTokenRepository.save(
            new RefreshTokenEntity(existMemberAuth.getMemberNo(),
                authTokenProvider.createRefreshToken(existMemberAuth.getMemberNo()).getToken()));

        // 응답에 토큰 정보 추가
        response.addHeader("Authorization", "Bearer " + accessToken.getToken());
        response.addHeader("refresh", "Bearer " + refreshToken.getRefreshToken());


    }

    /**
     * 닉네임 중복되는지 확인
     * @param nickname
     * @return
     */
    public boolean existsByNickname(String nickname) {
       return memberAuthRepository.existsByNickname(nickname);
    }

    public void updatePassword(ChangeAuthDto authDto, MemberAuthEntity member) {

        member.updatePassword(CryptoUtil.encrypt(authDto.password()));
        memberAuthRepository.save(member);
    }


    public void updateUser(UpdateUserRequestDto req, MemberAuthEntity member) {

        if (req.nickname() != null && !req.nickname().isBlank()) {
            if(!member.getNickname().equals(req.nickname())) {
                if(memberAuthRepository.existsByNickname(req.nickname())) {
                    throw new CustomException(ALREADY_NICKNAME);
                }
            }

        }
        member.updateUser(req);
        memberAuthRepository.save(member);

    }

    public void resetPassword(Long memberNo) {
        MemberAuthEntity existMemberAuth = memberAuthRepository.findByMemberNo(memberNo)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if (!existMemberAuth.getRole().isAdmin()) {
            throw new CustomException(NOT_ADMIN_OR_AVAILABLE);
        }

        existMemberAuth.updatePassword(CryptoUtil.encrypt(DEFAULT_PASSWORD));
        memberAuthRepository.save(existMemberAuth);
    }

    public void deleteAdmin(Long memberNo) {
        MemberAuthEntity existMemberAuth = memberAuthRepository.findByMemberNo(memberNo)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (!existMemberAuth.getRole().isAdmin()) {
            throw new CustomException(NOT_ADMIN_OR_AVAILABLE);
        }
        memberAuthRepository.deleteById(memberNo);
    }

//    public Page<GetAdminTableResponseDto> findAllExcludingRoleRoot(Pageable pageable) {
//        Page<MemberAuthEntity> adminPage = memberAuthRepository.findAllExcludingRoleRoot(pageable);
//        return adminPage.map(GetAdminTableResponseDto::from);
//
//
//    }

//    public Page<GetUserTableResponseDto> getUsers(Pageable pageable) {
//        return memberAuthRepository.getUsers(pageable);
//
//
//    }


}
