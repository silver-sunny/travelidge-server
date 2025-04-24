package com.studio.api.global.config.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration_time}")
    private Long tokenValidTime;
    @Value("${jwt.refresh.expiration_time}")
    private Long refreshTokenValidTime;

    @Bean
    public AuthTokenProvider authTokenProvider() {
        return new AuthTokenProvider(secret, tokenValidTime, refreshTokenValidTime);
    }

}
