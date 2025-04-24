package com.studio.api.global.config.security;


import com.studio.core.global.enums.AuthRole;
import com.studio.core.global.enums.ProviderType;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
// 토큰 데이터 클래스
public class AuthToken {
    @Getter
    private final String token;
    private final SecretKey key;
    private static final String AUTHORITIES_KEY = "role";

    public AuthToken(String token, SecretKey key) {
        this.key = key;
        this.token = token;
    }

    AuthToken(Long memberNo, Date expiry, SecretKey key) {
        this.key = key;
        this.token = createRefreshToken(memberNo, expiry);
    }




    AuthToken(Long memberNo, String id, AuthRole role, Date expiry, SecretKey key) {
        this.key = key;
        this.token = createAccessToken(memberNo, id, role, expiry);
    }

    AuthToken(Long memberNo, String nickname, ProviderType providerType, AuthRole role, Date expiry, SecretKey key) {
        this.key = key;
        this.token = createMemberAccessToken(memberNo, nickname,providerType ,role, expiry);
    }



    private String createMemberAccessToken(Long memberNo, String nickname,ProviderType providerType, AuthRole role, Date expiry) {


        Claims claims = Jwts.claims()
                .subject(memberNo.toString())
                .build();

        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiry)
                .claim(AUTHORITIES_KEY, role)
                .claim("nickname", nickname)
                .claim("providerType", providerType)
                .signWith(key)
                .compact();
    }

    private String createAccessToken(Long memberNo, String id, AuthRole role, Date expiry) {


        Claims claims = Jwts.claims()
                .subject(memberNo.toString())
                .build();

        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiry)
                .claim(AUTHORITIES_KEY, role)
                .claim("id", id)
                .signWith(key)
                .compact();
    }

    private String createRefreshToken(Long memberNo, Date expiry) {


        Claims claims = Jwts.claims()
                .subject(memberNo.toString())
                .build();

        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }



    // (4) 토큰이 유효한지 여부 검증
    public boolean isTokenValid() {
        return getValidTokenClaims() != null; // 유효하면 true
    }

    // (4) 토큰의 만료 여부 검증
    public boolean isTokenExpired() {
        return getExpiredTokenClaims() != null; // 만료됐으면 true
    }

    // (5) 만료되지 않은 토큰의 클레임 추출
    // JWT 구문 분석 과정에서 예외 발생 시, 해당 예외 처리 후 null 반환
    public Claims getValidTokenClaims() {
        try {

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();


        }catch (SecurityException | MalformedJwtException e) {
            log.debug("잘못된 Jwt 서명입니다.");
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
        } catch (ExpiredJwtException e) {
            log.debug("만료된 토큰입니다.");
            throw new CustomException(ErrorCode.JWT_EXPIRE_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.debug("지원하지 않는 토큰입니다.");
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
        } catch (IllegalArgumentException e) {
            log.debug("잘못된 토큰입니다.");
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
        }



    }



    // (6) 만료된 토큰의 클레임 추출
    public Claims getExpiredTokenClaims() {
        try {

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
            // 예외가 발생한 경우 (jwt 토큰의 만료 기간이 지났을 경우)
            // 로그 출력 후, 토큰의 클레임 대신 예외에서 가져온 클레임 반환
        }
    }
}