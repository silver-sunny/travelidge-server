package com.studio.core.global.toss;


import static com.studio.core.global.exception.ErrorCode.TOSS_REST_SEND_ERROR;

import com.studio.core.global.exception.CustomException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossApiService {

    @Value("${toss.signature.client-secret}")
    private String clientSecret;

    private final WebClient webClient;

    public Mono<HttpHeaders> createHeaders() {
        return Mono.fromSupplier(() -> {
            Base64.Encoder encoder = Base64.getEncoder();
            String encodedAuth = encoder.encodeToString((clientSecret + ":").getBytes(
                StandardCharsets.UTF_8));
            String authorization = "Basic " + encodedAuth;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            headers.setContentType(MediaType.APPLICATION_JSON);
            return headers;
        });
    }

    public <T> Mono<T> sendRequest(String url, HttpMethod method, Object body, Class<T> responseType) {
        return createHeaders().flatMap(headers -> {
            WebClient.RequestBodySpec requestSpec = webClient.method(method)
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers));

            if (body != null) {
                if (body instanceof MultiValueMap) {
                    requestSpec.contentType(MediaType.MULTIPART_FORM_DATA);
                }
                requestSpec.body(BodyInserters.fromValue(body));
            }

            return requestSpec
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        log.error("토스 API 호출 오류: {}", errorBody);
                        return Mono.error(new CustomException(TOSS_REST_SEND_ERROR, "토스 API 호출 오류: " + errorBody));
                    }))
                .bodyToMono(responseType);
        });
    }
}
