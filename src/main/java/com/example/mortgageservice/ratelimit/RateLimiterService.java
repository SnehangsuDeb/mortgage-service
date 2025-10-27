package com.example.mortgageservice.ratelimit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple fixed-window rate limiter.
 * Key is typically a combination of client IP and request path.
 */
@Component
public class RateLimiterService {

    private static final class CounterWindow {
        final long windowStartMs;
        final int count;

        CounterWindow(long windowStartMs, int count) {
            this.windowStartMs = windowStartMs;
            this.count = count;
        }
    }

    private final ConcurrentHashMap<String, CounterWindow> counters = new ConcurrentHashMap<>();

    @Value("${ratelimit.enabled:true}")
    private boolean enabled;

    @Value("${ratelimit.window-ms:60000}")
    private long windowMs;

    @Value("${ratelimit.max-requests:100}")
    private int maxRequests;

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns true if the request is allowed within the current window; false if rate-limited.
     */
    public boolean allow(String key) {
        if (!enabled) {
            return true;
        }
        final long now = System.currentTimeMillis();

        // Compute atomically per key
        final AllowHolder holder = new AllowHolder();
        counters.compute(key, (k, current) -> {
            if (current == null) {
                holder.allowed = true;
                return new CounterWindow(now, 1);
            }
            // Window reset
            if ((now - current.windowStartMs) >= windowMs) {
                holder.allowed = true;
                return new CounterWindow(now, 1);
            }
            // Within same window
            if (current.count < maxRequests) {
                holder.allowed = true;
                return new CounterWindow(current.windowStartMs, current.count + 1);
            } else {
                holder.allowed = false;
                return current;
            }
        });
        return holder.allowed;
    }

    private static final class AllowHolder {
        boolean allowed;
    }
}
