package com.keax.auth.infrastructure.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifica que la configuracion CORS acepte origenes exactos y rechace valores
 * vacios o comodines incompatibles con credenciales.
 */
class SecurityConfigTest {

    @Test
    void resolvesExactCorsOrigins() {
        // Act: se separa y normaliza una lista de origenes configurados.
        List<String> origins = SecurityConfig.resolveAllowedOrigins("http://localhost:5173, https://keax.dev");

        // Assert: se conservan exclusivamente los dos origenes exactos.
        assertEquals(List.of("http://localhost:5173", "https://keax.dev"), origins);
    }

    @Test
    void rejectsWildcardCorsOrigin() {
        // Act y Assert: un comodin total no puede combinarse con credenciales.
        assertThrows(IllegalStateException.class, () -> SecurityConfig.resolveAllowedOrigins("*"));
    }

    @Test
    void rejectsPatternCorsOrigin() {
        // Act y Assert: tampoco se permiten patrones parciales de subdominio.
        assertThrows(IllegalStateException.class, () -> SecurityConfig.resolveAllowedOrigins("https://*.keax.dev"));
    }

    @Test
    void rejectsEmptyCorsOrigins() {
        // Act y Assert: la aplicacion falla temprano si CORS queda sin origenes.
        assertThrows(IllegalStateException.class, () -> SecurityConfig.resolveAllowedOrigins("   "));
    }

}
