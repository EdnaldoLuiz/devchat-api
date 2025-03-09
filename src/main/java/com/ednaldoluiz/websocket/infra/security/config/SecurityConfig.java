package com.ednaldoluiz.websocket.infra.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.ednaldoluiz.websocket.infra.security.filter.JwtAuthFilter;
import com.ednaldoluiz.websocket.infra.security.filter.OAuth2AuthFilter;
import com.ednaldoluiz.websocket.infra.security.handler.CustomAccessDeniedHandler;
import com.ednaldoluiz.websocket.infra.security.handler.CustomAuthFailureHandler;
import com.ednaldoluiz.websocket.infra.security.handler.CustomLogoutHandler;
import com.ednaldoluiz.websocket.infra.security.service.CustomUserDetailsService;
import com.ednaldoluiz.websocket.shared.constants.BeanConstants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    static String[] SWAGGER_URLS = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**"
    };

    static String[] AUTH_WHITELIST = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/generate-password",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/reset-password"
    };

    CustomUserDetailsService customUserDetailsService;
    JwtAuthFilter jwtAuthFilter;
    OAuth2AuthFilter oauth2AuthFilter;
    CustomAuthFailureHandler authFailureHandler;
    CustomAccessDeniedHandler accessDeniedHandler;
    CustomLogoutHandler logoutHandler;

    @Bean(name = BeanConstants.Security.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider::authenticate;
    }

    @Bean(name = BeanConstants.Security.PASSWORD_ENCODER)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        CorsConfiguration cors = corsConfiguration();
        return http
                .securityMatcher("/api/v1/auth/oauth2/**", "/login/oauth2/**", "/oauth2/authorization/**")
                .cors(corsConfigurer -> corsConfigurer.configurationSource(request -> cors))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/oauth2/**").permitAll()
                        .requestMatchers("/login/oauth2/**").permitAll()
                        .anyRequest().denyAll()
                )
                .oauth2Login(oauth2 -> oauth2
                    .loginPage("/api/v1/auth/oauth2/login") // ex.: redirect
                    .defaultSuccessUrl("/api/v1/auth/oauth2/success", true)
                    .failureUrl("/api/v1/auth/oauth2/failure")
                )
                // Como essa chain é para OAuth2, NÃO setamos .authenticationManager(custom)
                .addFilterBefore(oauth2AuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authFailureHandler)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessUrl("/api/v1/auth/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                            (request, response, authentication) -> response.setStatus(HttpStatus.OK.value())
                        )
                )
                .build();
    }

    // ===============================================================
    // 2) "API" + SWAGGER + JWT SECURITY CHAIN -- /api/**, swagger, etc
    // ===============================================================
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        CorsConfiguration cors = corsConfiguration();
        return http
                .securityMatcher("/**")
                .cors(corsConfigurer -> corsConfigurer.configurationSource(request -> cors))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_URLS).permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers("/api/v1/auth/oauth2/**").permitAll() 
                        .requestMatchers("/login/oauth2/**").permitAll()
                        .requestMatchers("/actuator/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/auth/logout").authenticated()
                        .requestMatchers("/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authFailureHandler)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessUrl("/api/v1/auth/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                            (request, response, authentication) -> response.setStatus(HttpStatus.OK.value())
                        )
                )
                .build();
    }

    private CorsConfiguration corsConfiguration() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.applyPermitDefaultValues();
        cors.addAllowedMethod(HttpMethod.PUT);
        cors.addAllowedMethod(HttpMethod.PATCH);
        cors.addAllowedMethod(HttpMethod.GET);
        cors.addAllowedMethod(HttpMethod.DELETE);
        cors.addAllowedMethod(HttpMethod.POST);
        return cors;
    }
}