package com.keax.email.infrastructure.in.web.ratelimit;

import com.keax.shared.domain.exceptions.RateLimitExceededException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifica de forma determinista el limite, la ventana temporal y el aislamiento
 * por cliente del rate limiter de contacto.
 */
class ContactRateLimiterTest {

    @Test
    void blocksClientAfterMaxRequestsInsideWindow() {
        // Arrange: el cliente consume sus dos intentos permitidos.
        MutableClock clock = new MutableClock();
        ContactRateLimiter limiter = new ContactRateLimiter(2, 1000, clock);

        limiter.assertAllowed("127.0.0.1");
        limiter.assertAllowed("127.0.0.1");

        // Act y Assert: el tercer intento dentro de la ventana se bloquea.
        assertThrows(RateLimitExceededException.class, () -> limiter.assertAllowed("127.0.0.1"));
    }

    @Test
    void allowsClientAgainAfterWindowExpires() {
        // Arrange: se agota el cupo y se avanza mas alla de la ventana.
        MutableClock clock = new MutableClock();
        ContactRateLimiter limiter = new ContactRateLimiter(2, 1000, clock);

        limiter.assertAllowed("127.0.0.1");
        limiter.assertAllowed("127.0.0.1");
        clock.plusMillis(1001);

        // Act y Assert: el cliente recupera su capacidad.
        assertDoesNotThrow(() -> limiter.assertAllowed("127.0.0.1"));
    }

    @Test
    void tracksClientsSeparately() {
        // Arrange: el primer cliente agota su unico intento.
        MutableClock clock = new MutableClock();
        ContactRateLimiter limiter = new ContactRateLimiter(1, 1000, clock);

        limiter.assertAllowed("127.0.0.1");

        // Act y Assert: otro cliente mantiene un contador independiente.
        assertDoesNotThrow(() -> limiter.assertAllowed("127.0.0.2"));
    }

    private static class MutableClock extends Clock {

        private Instant instant = Instant.parse("2026-06-12T00:00:00Z");

        @Override
        public ZoneId getZone() {
            return ZoneId.of("UTC");
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }

        void plusMillis(long millis) {
            // Permite mover el tiempo de la prueba sin esperar realmente.
            instant = instant.plusMillis(millis);
        }

    }

}
