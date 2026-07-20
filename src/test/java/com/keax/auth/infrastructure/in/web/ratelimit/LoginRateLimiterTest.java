package com.keax.auth.infrastructure.in.web.ratelimit;

import com.keax.shared.domain.exceptions.RateLimitExceededException;
import org.junit.jupiter.api.Test;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginRateLimiterTest {

    @Test
    void blocksRepeatedLoginAttemptsAndCanResetAfterSuccess() {
        LoginRateLimiter limiter = new LoginRateLimiter(2, 60_000, 100, Clock.systemUTC());

        limiter.consumeAttempt("client:user");
        limiter.consumeAttempt("client:user");
        assertThrows(
                RateLimitExceededException.class,
                () -> limiter.consumeAttempt("client:user")
        );

        limiter.reset("client:user");
        assertDoesNotThrow(() -> limiter.consumeAttempt("client:user"));
    }
}
