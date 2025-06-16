package com.betafore.evoting.security_config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
        "/swagger-ui/**", "/swagger-ui.html",
        "/swagger-resources/**", "/swagger-resources",
        "/configuration-ui", "/configuration/security",
        "/webjars/**", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
        "/api/v1/auth/**","/api/v1/users/register/**", "/api/v1/organizers/login/**",
        "/api/v1/organizers/sendOtpToMail/**","/api/v1/organizers/resetPassword/**",
        "/api/v1/users/scan/**", "/api/v1/users/sendOtpToPhone/**",
        "/api/v1/users/otpValidation", "/api/v1/seminars/img/**",
        "/api/v1/gifts/img/**", "/api/v1/gala/img/**", "/api/v1/stalls/logo/**",
        "/api/v1/stalls/background/**", "/api/v1/vip/img/**",
        "/api/v1/prizes/img/**","/api/v1/otp/**"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors().and().csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling((exception) -> exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    WHITE_LIST_URL).permitAll()

                .anyRequest().authenticated()
            )
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout((logout) -> logout
                .addLogoutHandler(logoutHandler).logoutUrl("/api/v1/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));
        return http.build();
    }

}

