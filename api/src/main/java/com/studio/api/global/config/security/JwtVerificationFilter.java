package com.studio.api.global.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider authTokenProvider;
    private final CustomAdminDetailService customAdminDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenStr = HeaderUtils.getAccessToken(request); // AccessToken 추출해서 Bearer 제외하고 나머지 토큰으로 인식한 후에
        AuthToken token = authTokenProvider.convertAuthToken(tokenStr); // 해당 str로 토큰으로 변환

        // securityContextHolder에 유저 정보를 저장해주는 로직
        if (token.isTokenValid()) {
            MemberPrincipal userDetails = customAdminDetailService.loadUserByUsername(token.getValidTokenClaims().getSubject());


            Authentication authentication = authTokenProvider.getAuthentication(token, userDetails);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

            filterChain.doFilter(request, response);
   }

    // 특정 조건에서 필터를 건너뛰도록 설정하는 메서드
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String tokenStr = HeaderUtils.getAccessToken(request);
        return tokenStr == null;
        // 요청에 포함된 액세스 토큰을 가져와서 이 토큰이 null인 경우에만 필터를 건너뛰도록 설정
    }


}
