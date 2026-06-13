package com.keax.auth.infrastructure.config;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.keax.auth.infrastructure.in.web.filter.JwtAuthenticationFilter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Value("${spring.web.cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${spring.web.cors.allowed-methods}")
    private String allowedMethods;

    @Value("${spring.web.cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${spring.web.cors.allow-credentials}")
    private boolean allowCredentials;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/api/auth/login").permitAll();
            auth.requestMatchers("/api/portfolio/**").permitAll();
            auth.anyRequest().authenticated();
        });
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(resolveAllowedOrigins(allowedOrigins));
        configuration.setAllowedMethods(splitConfig(allowedMethods));
        configuration.setAllowedHeaders(splitConfig(allowedHeaders));
        configuration.setAllowCredentials(allowCredentials);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    static List<String> resolveAllowedOrigins(String value) {
        List<String> origins = splitConfig(value);

        if (origins.isEmpty()) {
            throw new IllegalStateException("At least one CORS allowed origin must be configured");
        }

        origins.stream()
                .filter(SecurityConfig::isOriginPattern)
                .findFirst()
                .ifPresent(origin -> {
                    throw new IllegalStateException("CORS allowed origins must be exact values, not patterns: " + origin);
                });

        return origins;
    }

    private static boolean isOriginPattern(String origin) {
        return "*".equals(origin) || origin.contains("*");
    }

    private static List<String> splitConfig(String value) {
        if (value == null) {
            return List.of();
        }

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .toList();
    }

}
