package com.keax.shared.infrastructure.in.web.ratelimit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;

public final class SlidingWindowRateLimiter {

    private final Cache<String, Deque<Long>> attemptsByClient;
    private final int maxRequests;
    private final long windowMs;
    private final Clock clock;

    public SlidingWindowRateLimiter(int maxRequests, long windowMs, long maxClients, Clock clock) {
        if (maxRequests < 1 || windowMs < 1 || maxClients < 1) {
            throw new IllegalArgumentException("Rate limit values must be greater than zero");
        }

        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
        this.clock = clock;
        this.attemptsByClient = Caffeine.newBuilder()
                .maximumSize(maxClients)
                .expireAfterAccess(Duration.ofMillis(windowMs))
                .build();
    }

    public boolean tryConsume(String clientKey) {
        String safeClientKey = clientKey == null || clientKey.isBlank() ? "unknown" : clientKey;
        long now = clock.millis();
        Deque<Long> attempts = attemptsByClient.get(safeClientKey, key -> new ArrayDeque<>());

        synchronized (attempts) {
            removeExpiredAttempts(attempts, now);
            if (attempts.size() >= maxRequests) {
                return false;
            }
            attempts.addLast(now);
            return true;
        }
    }

    public void reset(String clientKey) {
        if (clientKey != null) {
            attemptsByClient.invalidate(clientKey);
        }
    }

    long estimatedClientCount() {
        return attemptsByClient.estimatedSize();
    }

    private void removeExpiredAttempts(Deque<Long> attempts, long now) {
        long oldestAllowedAttempt = now - windowMs;
        while (!attempts.isEmpty() && attempts.peekFirst() <= oldestAllowedAttempt) {
            attempts.removeFirst();
        }
    }
}
