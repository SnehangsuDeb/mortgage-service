package com.example.mortgageservice.ratelimit;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterServiceTest {

    @Test
    void shouldAllowUpToMaxRequestsWithinWindow_thenBlockNext() {
        RateLimiterService svc = new RateLimiterService();
        // Configure a small window and small max for deterministic testing
        ReflectionTestUtils.setField(svc, "enabled", true);
        ReflectionTestUtils.setField(svc, "windowMs", 50L);
        ReflectionTestUtils.setField(svc, "maxRequests", 2);

        String key = "127.0.0.1:/api/test";

        assertTrue(svc.allow(key), "1st request should be allowed");
        assertTrue(svc.allow(key), "2nd request should be allowed");
        assertFalse(svc.allow(key), "3rd request within same window should be blocked");
    }

    @Test
    void shouldResetCounterAfterWindowElapsed() throws InterruptedException {
        RateLimiterService svc = new RateLimiterService();
        ReflectionTestUtils.setField(svc, "enabled", true);
        // Very short window for test
        ReflectionTestUtils.setField(svc, "windowMs", 10L);
        ReflectionTestUtils.setField(svc, "maxRequests", 1);

        String key = "127.0.0.1:/api/test";

        assertTrue(svc.allow(key), "First request in new window allowed");
        assertFalse(svc.allow(key), "Second request in same window blocked");

        // Wait for the window to elapse
        Thread.sleep(20L);

        assertTrue(svc.allow(key), "After window elapses, request should be allowed again");
    }
}
