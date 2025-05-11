package com.studio.api.global.config.swagger;

import static com.studio.core.global.exception.ErrorCode.PARAMETER_VALIDATION_ERROR;
import static java.util.stream.Collectors.groupingBy;

import com.studio.api.global.config.annotaions.CustomExceptionDescription;
import com.studio.api.global.config.annotaions.DisableSwaggerSecurity;
import com.studio.core.global.exception.ErrorCode;
import com.studio.core.global.response.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {



    @Bean
    public OpenAPI openAPI(ServletContext servletContext) {
        Server localServer = new Server();
        Server devServer = new Server();
        Server prodServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("local Server");
        devServer.setUrl("https://api.travelidge.shop");
        devServer.setDescription("dev Server");
        return new OpenAPI()
                .info(_setApiInfo()) // swagger info 설정
                .components(_setComponents()) // auth token 설정
                .addSecurityItem(_setSecurityItems()) // 각 api에 item 추가
                .servers(List.of(localServer,devServer,prodServer));
    }

    private Info _setApiInfo() {
        return new Info()
                .version("1.0.0")
                .title("ticket API 명세서")
                .description("ticket 서비스 API 명세서입니다.");
    }

    private Components _setComponents() {
        return new Components()
                .addSecuritySchemes("access-token", _getJwtSecurityScheme());
    }

    private SecurityScheme _getJwtSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }

    private SecurityRequirement _setSecurityItems() {
        return new SecurityRequirement()
                .addList("access-token");
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {

            CustomExceptionDescription customExceptionDescription = handlerMethod.getMethodAnnotation(CustomExceptionDescription.class);
            DisableSwaggerSecurity methodAnnotation = handlerMethod.getMethodAnnotation(DisableSwaggerSecurity.class);

            // DisableSecurity 어노테이션있을시 스웨거 시큐리티 설정 삭제
            if (methodAnnotation != null) {
                operation.setSecurity(Collections.emptyList());
            }

            // CustomExceptionDescription 어노테이션 단 메소드 적용
            if (customExceptionDescription != null) {
                generateErrorCodeResponseExample(operation, customExceptionDescription.value());
            }

            return operation;
        };
    }

    private void generateErrorCodeResponseExample(
            Operation operation, SwaggerResponseDescription type) {

        ApiResponses responses = operation.getResponses();

        Set<ErrorCode> errorCodeList = type.getErrorCodeList();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders =
                errorCodeList.stream()
                        .map(
                                errorCode -> {
                                    return ExampleHolder.builder()
                                            .holder(
                                                    getSwaggerExample(errorCode))
                                            .code(errorCode.getHttpCode())
                                            .name(errorCode.toString())
                                            .build();
                                }
                        ).collect(groupingBy(ExampleHolder::getCode));
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaggerExample(ErrorCode errorCode) {
        Map<String, String> result = new LinkedHashMap<>();

        if (errorCode.getErrorCode() == PARAMETER_VALIDATION_ERROR.getErrorCode()) {
            result.put("key", "검증 대상 파라미터");
            result.put("value", "받은 파라미터 값");
            result.put("reason", "검증 에러 원인 메세지");
        }

        ErrorResponse errorResponse = new ErrorResponse(errorCode.getErrorCode(), errorCode.getMessage(), result);
        Example example = new Example();
        example.description(errorCode.getMessage());
        example.setValue(errorResponse);
        return example;
    }

    private void addExamplesToResponses(
            ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();
                    v.forEach(
                            exampleHolder -> {
                                mediaType.addExamples(
                                        exampleHolder.getName(), exampleHolder.getHolder());
                            });
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setDescription("");
                    apiResponse.setContent(content);
                    responses.addApiResponse(status.toString(), apiResponse);
                });
    }

    @Bean
    public GroupedOpenApi getMemberApi() {

        return GroupedOpenApi
                .builder()
                .group("member")
                .pathsToMatch("/v1/api/admin/**","/v1/api/user/**" )
                .build();

    }

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi
                .builder()
                .group("product")
                .pathsToMatch("/v1/api/product/**", "/v1/api/naver/product/**")
                .build();
    }

    @Bean
    public GroupedOpenApi clientProductApi() {
        return GroupedOpenApi
            .builder()
            .group("client product,order,cart,payment,my,inquiry")
            .pathsToMatch("/v1/api/client/product/**", "/v1/api/cart/**",
                "/v1/api/client/product-order/**", "/v1/api/client/payment/**", "/v1/api/client/favorite/**",
                "/v1/api/my/**","/v1/api/client/inquiry/**", "/v1/api/client/search/**", "/v1/api/client/report/**", "/v1/api/client/**")
            .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi
                .builder()
                .group("order")
                .pathsToMatch("/v1/api/product-order/**", "/v1/api/naver/product-order/**")
                .build();
    }

    @Bean
    public GroupedOpenApi ticketApi() {
        return GroupedOpenApi
                .builder()
                .group("ticket")
                .pathsToMatch("/v1/api/ticket/**")
                .build();
    }

    @Bean
    public GroupedOpenApi getInquiryApi() {

        return GroupedOpenApi
                .builder()
                .group("inquiry")
                .pathsToMatch("/v1/api/inquiry/**")
                .build();

    }
    @Bean
    public GroupedOpenApi recommendedApi() {
        return GroupedOpenApi
                .builder()
                .group("recommended")
                .pathsToMatch("/v1/api/recommended/**")
                .build();
    }

    @Bean
    public GroupedOpenApi enumsApi() {
        return GroupedOpenApi
                .builder()
                .group("ENUMS")
                .pathsToMatch("/v1/api/enums/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reportApi() {
        return GroupedOpenApi
            .builder()
            .group("report")
            .pathsToMatch("/v1/api/report/**")
            .build();
    }

}
