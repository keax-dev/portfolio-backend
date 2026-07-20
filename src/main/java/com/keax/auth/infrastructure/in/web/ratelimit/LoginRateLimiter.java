package com.keax.auth.infrastructure.in.web.ratelimit;

import com.keax.shared.domain.exceptions.RateLimitExceededException;
import com.keax.shared.infrastructure.in.web.ratelimit.SlidingWindowRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class LoginRateLimiter {

    private final SlidingWindowRateLimiter delegate;

    public LoginRateLimiter(
            @Value("${app.rate-limit.login.max-requests:5}") int maxRequests,
            @Value("${app.rate-limit.login.window-ms:900000}") long windowMs,
            @Value("${app.rate-limit.login.max-clients:10000}") long maxClients,
            Clock clock
    ) {
        this.delegate = new SlidingWindowRateLimiter(maxRequests, windowMs, maxClients, clock);
    }

    public void consumeAttempt(String clientKey) {
        if (!delegate.tryConsume(clientKey)) {
            throw new RateLimitExceededException("Too many login attempts. Please try again later.");
        }
    }

    public void reset(String clientKey) {
        delegate.reset(clientKey);
    }
}
