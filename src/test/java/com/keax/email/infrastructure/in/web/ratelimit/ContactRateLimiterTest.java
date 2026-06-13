package com.keax.email.infrastructure.in.web.ratelimit;

import com.keax.shared.domain.exceptions.RateLimitExceededException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContactRateLimiterTest {

    @Test
    void blocksClientAfterMaxRequestsInsideWindow() {
        MutableClock clock = new MutableClock();
        ContactRateLimiter limiter = new ContactRateLimiter(2, 1000, clock);

        limiter.assertAllowed("127.0.0.1");
        limiter.assertAllowed("127.0.0.1");

        assertThrows(RateLimitExceededException.class, () -> limiter.assertAllowed("127.0.0.1"));
    }

    @Test
    void allowsClientAgainAfterWindowExpires() {
        MutableClock clock = new MutableClock();
        ContactRateLimiter limiter = new ContactRateLimiter(2, 1000, clock);

        limiter.assertAllowed("127.0.0.1");
        limiter.assertAllowed("127.0.0.1");
        clock.plusMillis(1001);

        assertDoesNotThrow(() -> limiter.assertAllowed("127.0.0.1"));
    }

    @Test
    void tracksClientsSeparately() {
        MutableClock clock = new MutableClock();
        ContactRateLimiter limiter = new ContactRateLimiter(1, 1000, clock);

        limiter.assertAllowed("127.0.0.1");

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
            instant = instant.plusMillis(millis);
        }

    }

}
