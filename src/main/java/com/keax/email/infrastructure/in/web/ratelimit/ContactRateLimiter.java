package com.keax.email.infrastructure.in.web.ratelimit;

import com.keax.shared.domain.exceptions.RateLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContactRateLimiter {

    private final Map<String, Deque<Long>> attemptsByClient = new ConcurrentHashMap<>();
    private final int maxRequests;
    private final long windowMs;
    private final Clock clock;

    public ContactRateLimiter(
            @Value("${app.rate-limit.contact.max-requests:3}") int maxRequests,
            @Value("${app.rate-limit.contact.window-ms:600000}") long windowMs,
            Clock clock
    ) {
        if (maxRequests < 1) {
            throw new IllegalArgumentException("maxRequests must be greater than 0");
        }
        if (windowMs < 1) {
            throw new IllegalArgumentException("windowMs must be greater than 0");
        }

        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
        this.clock = clock;
    }

    public void assertAllowed(String clientKey) {
        String safeClientKey = clientKey == null || clientKey.isBlank() ? "unknown" : clientKey;
        long now = clock.millis();
        Deque<Long> attempts = attemptsByClient.computeIfAbsent(safeClientKey, key -> new ArrayDeque<>());

        synchronized (attempts) {
            removeExpiredAttempts(attempts, now);

            if (attempts.size() >= maxRequests) {
                throw new RateLimitExceededException("Too many contact attempts. Please try again later.");
            }

            attempts.addLast(now);
        }
    }

    private void removeExpiredAttempts(Deque<Long> attempts, long now) {
        long oldestAllowedAttempt = now - windowMs;

        while (!attempts.isEmpty() && attempts.peekFirst() <= oldestAllowedAttempt) {
            attempts.removeFirst();
        }
    }

}
