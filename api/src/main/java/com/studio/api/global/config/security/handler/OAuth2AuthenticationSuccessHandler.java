package com.studio.api.global.config.security.handler;


import com.studio.api.global.config.security.AuthToken;
import com.studio.api.global.config.security.AuthTokenProvider;
import com.studio.api.global.config.security.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.studio.api.global.config.security.oauth.OAuth2UserInfo;
import com.studio.api.global.config.security.oauth.OAuth2UserInfoFactory;
import com.studio.core.global.enums.AuthRole;
import com.studio.core.global.enums.ProviderType;
import com.studio.core.global.exception.CustomException;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.member.entity.RefreshTokenEntity;
import com.studio.core.member.repository.MemberAuthJpaRepository;
import com.studio.core.member.repository.RefreshTokenJpaRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.studio.core.global.exception.ErrorCode.MEMBER_SIGN_FAIL;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider jwtTokenProvider;
    private final MemberAuthJpaRepository memberAuthJpaRepository;
    private final RefreshTokenJpaRepository refreshTokenRepository;

    //로그인 후 가려던 페이지로 보내거나 홈화면으로 보내기
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        String redirectUri = getRedirectUriByCookie(cookies);

        // 로그인한 사용자 정보를 가져옴
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = authToken.getPrincipal();
        ProviderType providerType = ProviderType.valueOf(
            authToken.getAuthorizedClientRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType,
            oauth2User.getAttributes());
        String providerId = userInfo.getProviderId();
        String email = userInfo.getEmail();
        Optional<MemberAuthEntity> authOptional = memberAuthJpaRepository.findByProviderIdAndProviderType(
            providerId, providerType);

        if(providerId == null || providerId.isEmpty()) {
            throw new CustomException(MEMBER_SIGN_FAIL);
        }
        MemberAuthEntity auth = authOptional.orElseGet(() -> {
            MemberAuthEntity newUser = MemberAuthEntity.builder()
                .providerId(providerId)
                .providerType(providerType)
                .nickname(generateRandomNickname())
                .role(AuthRole.ROLE_USER)
                .email(email)
                .build();

            return memberAuthJpaRepository.save(newUser);
        });

        if (auth.getBanEndAt() != null && auth.getBanEndAt().isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.BANNED_USER);
        }

        RefreshTokenEntity userRefreshToken = refreshTokenRepository.save(new RefreshTokenEntity(
            auth.getMemberNo(),
            jwtTokenProvider.createRefreshToken(auth.getMemberNo()).getToken()
        ));

        AuthToken accessToken = jwtTokenProvider.createMemberAccessToken(auth.getMemberNo(),
            auth.getNickname(), providerType, auth.getRole());
        String refreshToken = userRefreshToken.getRefreshToken();

        // 쿼리 파라미터에 토큰 추가하여 리디렉트 URL 생성
        return UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("Authorization", accessToken.getToken())
            .queryParam("refresh", refreshToken)
            .build()
            .toUriString();
    }


    private String getRedirectUriByCookie(Cookie[] cookies) {

        return Arrays.stream(cookies)
            .filter(c -> c.getName().equals("redirect_uri"))
            .map(Cookie::getValue)
            .findAny().orElseThrow(NullPointerException::new);

    }

    protected void clearAuthenticationAttributes(HttpServletRequest request,
        HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }


    private String generateRandomNickname() {
        String nickname;
        do {
            nickname = "User_" + UUID.randomUUID().toString().substring(0, 12);
        } while (memberAuthJpaRepository.existsByNickname(nickname)); // 닉네임 중복 방지
        return nickname;
    }
}