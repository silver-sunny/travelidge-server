package com.studio.api.global.config.security.oauth;



import com.studio.core.global.enums.ProviderType;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {

        switch (providerType) {
            case GOOGLE:
                return new GoogleUserInfo(attributes);
            case NAVER:
                return new NaverOAuth2UserInfo(attributes);
            case KAKAO:
                return new KakaoUserInfo(attributes);
            default:
                throw new IllegalArgumentException("지원되지 않는 providerType: " + providerType);
        }
    }
}
