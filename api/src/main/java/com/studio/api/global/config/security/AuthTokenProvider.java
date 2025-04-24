package com.studio.api.global.config.security;

import com.studio.core.global.enums.AuthRole;
import com.studio.core.global.enums.ProviderType;
import com.studio.core.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.studio.core.global.exception.ErrorCode.JWT_ERROR_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class AuthTokenProvider {


    private final SecretKey key;
    private final long tokenValidTime;
    private final long refreshTokenValidTime;

    public AuthTokenProvider(String secret, long tokenValidTime, long refreshTokenValidTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        // 주어진 비밀키를 사용하여 HMAC-SHA 기반의 키 생성
        this.tokenValidTime = tokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }


    // 액세스 토큰 생성
    public AuthToken createAccessToken(Long memberNo, String id, AuthRole role ) {
        return new AuthToken(memberNo,id,role, new Date(System.currentTimeMillis() + tokenValidTime), key);
    }

    // 액세스 토큰 생성
    public AuthToken createMemberAccessToken(Long memberNo, String nickname, ProviderType providerType, AuthRole role ) {
        return new AuthToken(memberNo,nickname,providerType,role, new Date(System.currentTimeMillis() + tokenValidTime), key);
    }


    // 리프레시 토큰 생성
    public AuthToken createRefreshToken(Long memberNo) {
        return new AuthToken(memberNo, new Date(System.currentTimeMillis() + refreshTokenValidTime), key);
    }

    // 토큰 문자열로 AuthToken 객체 만드는 메서드 ( 토큰 검증 및 관련 작업 가능 )
    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication  getAuthentication(AuthToken authToken, MemberPrincipal userPrincipal) {

        if (authToken.isTokenValid()) {

            Claims claims = authToken.getValidTokenClaims();

            log.debug("claims subject := [{}]", claims.getSubject());


            return new UsernamePasswordAuthenticationToken(userPrincipal, authToken, userPrincipal.getAuthorities());
        } else {
            throw new CustomException(JWT_ERROR_TOKEN);

        }
    }



}