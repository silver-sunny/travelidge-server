package com.studio.core.global.naver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NaverOAuthTokenResponseDto {

    private String accessToken;

    private String expiresIn;

    private String tokenType;
}
