package com.keax.email.infrastructure.in.web.ratelimit;

import com.keax.shared.domain.exceptions.RateLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.infrastructure.in.web.ratelimit.SlidingWindowRateLimiter;

import java.time.Clock;
@Component
public class ContactRateLimiter {

    private final SlidingWindowRateLimiter delegate;

    @Autowired
    public ContactRateLimiter(
            @Value("${app.rate-limit.contact.max-requests:3}") int maxRequests,
            @Value("${app.rate-limit.contact.window-ms:600000}") long windowMs,
            @Value("${app.rate-limit.contact.max-clients:10000}") long maxClients,
            Clock clock
    ) {
        this.delegate = new SlidingWindowRateLimiter(maxRequests, windowMs, maxClients, clock);
    }

    public ContactRateLimiter(int maxRequests, long windowMs, Clock clock) {
        this(maxRequests, windowMs, 10_000, clock);
    }

    public void assertAllowed(String clientKey) {
        if (!delegate.tryConsume(clientKey)) {
            throw new RateLimitExceededException("Too many contact attempts. Please try again later.");
        }
    }
}
