package com.keax.auth.infrastructure.out.security;

import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Comprueba firma, lectura, expiracion y requisitos criptograficos de los JWT
 * sin depender del reloj real del equipo.
 */
class JwtUtilTest {

    private static final String SECRET =
            "a-secure-test-secret-with-more-than-thirty-two-bytes-1234567890";
    private static final Instant NOW = Instant.parse("2026-07-05T12:00:00Z");

    @Test
    void generatesAndValidatesTokenForSubject() {
        // Arrange: se fija el tiempo para obtener un token reproducible.
        JwtUtil jwtUtil = jwtAt(NOW, 60_000);

        // Act: se genera y valida el token.
        String token = jwtUtil.generateToken("admin");

        // Assert: la firma es valida y el subject se conserva.
        assertTrue(jwtUtil.validateToken(token));
        assertEquals("admin", jwtUtil.extractUsername(token));
    }

    @Test
    void rejectsExpiredToken() {
        // Arrange: el emisor crea un token corto y el validador avanza dos minutos.
        String token = jwtAt(NOW, 60_000).generateToken("admin");
        JwtUtil laterValidator = jwtAt(NOW.plusSeconds(120), 60_000);

        // Act y Assert: el token expirado ya no es aceptado.
        assertFalse(laterValidator.validateToken(token));
    }

    @Test
    void rejectsTamperedToken() {
        // Arrange: se altera la firma de un token valido.
        JwtUtil jwtUtil = jwtAt(NOW, 60_000);
        String token = jwtUtil.generateToken("admin");
        String tampered = token.substring(0, token.length() - 1) + "x";

        // Act y Assert: la modificacion invalida la firma.
        assertFalse(jwtUtil.validateToken(tampered));
    }

    @Test
    void rejectsWeakSigningSecret() {
        // Act y Assert: JJWT impide arrancar con una clave HMAC insegura.
        assertThrows(
                WeakKeyException.class,
                () -> new JwtUtil("short-secret", 60_000, Clock.fixed(NOW, ZoneOffset.UTC))
        );
    }

    private JwtUtil jwtAt(Instant instant, long expirationMs) {
        // Crea la unidad bajo prueba con un reloj controlado.
        return new JwtUtil(SECRET, expirationMs, Clock.fixed(instant, ZoneOffset.UTC));
    }

}
