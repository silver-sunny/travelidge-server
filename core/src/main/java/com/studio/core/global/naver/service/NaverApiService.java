package com.studio.core.global.naver.service;


import com.studio.core.global.exception.CustomException;
import com.studio.core.global.naver.dto.NaverOAuthTokenResponseDto;
import com.studio.core.global.utils.SignatureGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.studio.core.global.exception.ErrorCode.NAVER_REST_SEND_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverApiService {

    private static final String API_URL = "https://api.commerce.naver.com/external/v1/oauth2/token";
    private static final String GRANT_TYPE = "client_credentials";
    private static final String TYPE = "SELF";

    @Value("${naver.signature.client-id}")
    private String clientId;

    @Value("${naver.signature.client-secret}")
    private String clientSecret;

    private final WebClient webClient;

    public Mono<NaverOAuthTokenResponseDto> getOAuthToken() {
        long timestamp = getTimestamp() - 10000;
        String signature = SignatureGeneratorUtil.generateSignature(clientId, clientSecret, timestamp);

        return webClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("timestamp", String.valueOf(timestamp))
                        .with("client_secret_sign", signature)
                        .with("grant_type", GRANT_TYPE)
                        .with("type", TYPE))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("네이버 API 호출 오류: {}", errorBody);
                            return Mono.error(new CustomException(NAVER_REST_SEND_ERROR, "네이버 API 호출 오류: " + errorBody));
                        }))
                .bodyToMono(NaverOAuthTokenResponseDto.class)
                .doOnError(e -> log.error("Error while obtaining OAuth token: {}", e.getMessage(), e));
    }

    private long getTimestamp() {
        return System.currentTimeMillis();
    }

    public Mono<HttpHeaders> createHeaders() {
        return getOAuthToken().map(token -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token.getAccessToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            return headers;
        });
    }

    public <T> Mono<T> sendRequest(String url, HttpMethod method, Object body, Class<T> responseType) {
        return createHeaders().flatMap(headers -> {
            WebClient.RequestBodySpec requestSpec = webClient.method(method)
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers));

            // body가 null이 아닐 때만 설정
            if (body != null) {
                if (body instanceof MultiValueMap) {
                    requestSpec.contentType(MediaType.MULTIPART_FORM_DATA); // multipart/form-data 설정
                }
                requestSpec.body(BodyInserters.fromValue(body));
            }

            return requestSpec
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        log.error("네이버 API 호출 오류: {}", errorBody);
                        return Mono.error(new CustomException(NAVER_REST_SEND_ERROR, "네이버 API 호출 오류: " + errorBody));
                    }))
                .bodyToMono(responseType);
        });
    }

}

