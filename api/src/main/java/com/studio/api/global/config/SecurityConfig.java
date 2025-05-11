package com.studio.api.global.config;


import com.studio.api.global.config.security.CorsProperties;
import com.studio.api.global.config.security.JwtExceptionHandlerFilter;
import com.studio.api.global.config.security.JwtVerificationFilter;
import com.studio.api.global.config.security.handler.JwtAccessDeniedHandler;
import com.studio.api.global.config.security.handler.JwtAuthenticationEntryPoint;
import com.studio.api.global.config.security.handler.OAuth2AuthenticationFailureHandler;
import com.studio.api.global.config.security.handler.OAuth2AuthenticationSuccessHandler;
import com.studio.api.global.config.security.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.studio.core.global.enums.AuthRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsProperties corsProperties;

    private final JwtVerificationFilter filter;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final String[] PUBLIC_GET = {
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/oauth2/**",
            "/login/oauth2/**",

            "/v1/api/client/product/**",

            "/v1/api/recommended/client",
            "/v1/api/client/inquiry/**",
            "/v1/api/enums/**",
            "/v1/api/client/search/**",
            "/v1/api/client/review/{productId}"
    };

    private final String[] PUBLIC_POST = {
            "/v1/api/admin/login"
    };

    private final String[] ADMIN_URL = {
            "/v1/api/admin/**",
            "/v1/api/recommended/**",
            "/v1/api/inquiry/**",
            "/v1/api/report/**"
    };

    private final String[] USER_URL = {
            "/v1/api/cart/**",
            "/v1/api/client/product-order/**",
            "/v1/api/client/favorite/**",
            "/v1/api/my/**",
            "/v1/api/client/v1/api/client/review/**",
            "/v1/api/client/**",
            "/v1/api/client/report/**",
            "/v1/api/client/images/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST).permitAll()
                        .requestMatchers(ADMIN_URL)
                        .hasAnyAuthority(AuthRole.ROLE_ROOT.name(), AuthRole.ROLE_MANAGE.name(),
                                AuthRole.ROLE_GUEST.name())
                        .requestMatchers(USER_URL).hasAnyAuthority(AuthRole.ROLE_USER.name())
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)) // 세션 관리 상태 없음
                .formLogin(form -> form.disable()) // FormLogin 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // BasicHttp 비활성화
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                                .accessDeniedHandler(new JwtAccessDeniedHandler())

                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization")
                                .authorizationRequestRepository(authorizationRequestRepository)
                        )
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/*/oauth2/code/*")
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionHandlerFilter(), JwtVerificationFilter.class);
        return http.build();
    }

//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // Allow all origins (you can restrict it to specific domains as needed)
//        configuration.addAllowedOriginPattern(
//            "*"); // You can replace "*" with specific origins, e.g. "http://localhost:3000"
//
//        // Allow all HTTP methods
//        configuration.addAllowedMethod("*");
//
//        // Allow all headers
//        configuration.addAllowedHeader("*");
//
//        // Expose the Authorization and Refresh-Token headers to the client
//        configuration.setExposedHeaders(List.of("Authorization", "refresh"));
//
//        // Allow credentials (cookies, authorization headers, etc.)
//        configuration.setAllowCredentials(true);
//
//        // Register this CORS configuration for all endpoints
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }
//

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setExposedHeaders(Arrays.asList(corsProperties.getExposeHeaders().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);

        return corsConfigSource;
    }

}
