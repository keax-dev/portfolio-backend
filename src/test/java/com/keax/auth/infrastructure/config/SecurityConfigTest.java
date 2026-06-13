package com.keax.auth.infrastructure.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecurityConfigTest {

    @Test
    void resolvesExactCorsOrigins() {
        List<String> origins = SecurityConfig.resolveAllowedOrigins("http://localhost:5173, https://keax.dev");

        assertEquals(List.of("http://localhost:5173", "https://keax.dev"), origins);
    }

    @Test
    void rejectsWildcardCorsOrigin() {
        assertThrows(IllegalStateException.class, () -> SecurityConfig.resolveAllowedOrigins("*"));
    }

    @Test
    void rejectsPatternCorsOrigin() {
        assertThrows(IllegalStateException.class, () -> SecurityConfig.resolveAllowedOrigins("https://*.keax.dev"));
    }

    @Test
    void rejectsEmptyCorsOrigins() {
        assertThrows(IllegalStateException.class, () -> SecurityConfig.resolveAllowedOrigins("   "));
    }

}
