package com.studio.api.member.service;


import static com.studio.api.global.config.security.HeaderUtils.getAccessToken;
import static com.studio.api.global.config.security.HeaderUtils.getHeaderRefreshToken;
import static com.studio.core.global.exception.ErrorCode.JWT_ERROR_TOKEN;
import static com.studio.core.global.exception.ErrorCode.JWT_NOT_EXPIRE_TOKEN;
import static com.studio.core.global.exception.ErrorCode.JWT_UNMATCHED_CLAIMS;

import com.studio.api.global.config.security.AuthToken;
import com.studio.api.global.config.security.AuthTokenProvider;
import com.studio.core.global.enums.AuthRole;
import com.studio.core.global.exception.CustomException;
import com.studio.core.member.entity.RefreshTokenEntity;
import com.studio.core.member.repository.RefreshTokenJpaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final AuthTokenProvider authTokenProvider;

    private final RefreshTokenJpaRepository refreshTokenRepository;

    // 리프레시 토큰으로 액세스 토큰 갱신
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        AuthToken accessToken = authTokenProvider.convertAuthToken(getAccessToken(request));
        validateAccessTokenCheck(accessToken);
        Long memberNo = Long.valueOf(accessToken.getExpiredTokenClaims().getSubject());

        RefreshTokenEntity refreshToken = refreshTokenRepository.findByMemberNo(memberNo).orElseThrow(() -> new CustomException(JWT_UNMATCHED_CLAIMS));;

        validateRefreshTokenCheck(refreshToken, authTokenProvider.convertAuthToken(getHeaderRefreshToken(request)));
        // 새 엑세스 토큰 생성
        AuthToken newAccessToken = authTokenProvider.createAccessToken(memberNo,String.valueOf(accessToken.getExpiredTokenClaims().get("id")), AuthRole.valueOf(String.valueOf(accessToken.getExpiredTokenClaims().get("role"))));
        response.addHeader("Authorization", "Bearer " + newAccessToken.getToken());
    }
    // 엑세스 토큰 유효성 확인
    public void validateAccessTokenCheck(AuthToken authToken) {
        if (!authToken.isTokenExpired()) // 만료되지 않았다면 에러 (만료가 되어야 리프레시 토큰으로 다시 발급받을 수 있기 때문)
            throw new CustomException(JWT_NOT_EXPIRE_TOKEN);
        if (authToken.getExpiredTokenClaims() == null) // 만료된 토큰의 클레임이 null인지 확인

            throw new CustomException(JWT_ERROR_TOKEN);
        // 토큰이 만료되었을 때, 해당 토큰의 클레임을 가져올 수 있다면 이는 토큰이 잘못된 것으로 간주
    }

    // 리프레시 토큰 유효성 검증
    public void validateRefreshTokenCheck(RefreshTokenEntity refreshToken, AuthToken headerRefreshToken) {
        if(!headerRefreshToken.isTokenValid()) // 만료되었다면 에러
            throw new CustomException(JWT_ERROR_TOKEN);
        if (!refreshToken.getRefreshToken().equals(headerRefreshToken.getToken())) // 리프레시 토큰이 같지 않다면 에러
            throw new CustomException(JWT_UNMATCHED_CLAIMS);
    }
}
