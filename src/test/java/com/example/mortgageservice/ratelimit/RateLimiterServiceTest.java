package com.example.mortgageservice.ratelimit;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    void shouldAllowAllRequestsWhenDisabled() {
        RateLimiterService svc = new RateLimiterService();
        ReflectionTestUtils.setField(svc, "enabled", false);
        ReflectionTestUtils.setField(svc, "windowMs", 100L);
        ReflectionTestUtils.setField(svc, "maxRequests", 1);

        String key = "10.0.0.1:/api/anything";
        for (int i = 0; i < 10; i++) {
            assertTrue(svc.allow(key), "Disabled limiter must allow all requests");
        }
    }

    @Test
    void shouldIsolateCountsPerKeyWithinWindow() {
        RateLimiterService svc = new RateLimiterService();
        ReflectionTestUtils.setField(svc, "enabled", true);
        ReflectionTestUtils.setField(svc, "windowMs", 200L);
        ReflectionTestUtils.setField(svc, "maxRequests", 2);

        String k1 = "k1";
        String k2 = "k2";

        // Key 1
        assertTrue(svc.allow(k1));
        assertTrue(svc.allow(k1));
        assertFalse(svc.allow(k1));

        // Key 2 unaffected by Key 1 usage
        assertTrue(svc.allow(k2));
        assertTrue(svc.allow(k2));
        assertFalse(svc.allow(k2));
    }

    @Test
    void shouldThrowNullPointer_whenKeyIsNull() {
        RateLimiterService svc = new RateLimiterService();
        ReflectionTestUtils.setField(svc, "enabled", true);
        assertThrows(NullPointerException.class, () -> svc.allow(null));
    }

    @Test
    void shouldNotAllowMoreThanMaxConcurrently() throws InterruptedException {
        RateLimiterService svc = new RateLimiterService();
        ReflectionTestUtils.setField(svc, "enabled", true);
        ReflectionTestUtils.setField(svc, "windowMs", 500L);
        ReflectionTestUtils.setField(svc, "maxRequests", 10);

        String key = "burst-key";
        int threads = 50;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicInteger allowed = new AtomicInteger();

        for (int i = 0; i < threads; i++) {
            pool.execute(() -> {
                try {
                    start.await();
                    if (svc.allow(key)) {
                        allowed.incrementAndGet();
                    }
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        // Fire all threads within the same window
        start.countDown();
        assertTrue(done.await(2, TimeUnit.SECONDS), "All tasks should complete quickly");
        pool.shutdownNow();

        assertEquals(10, allowed.get(), "Allowed count must not exceed maxRequests");
    }

    @Test
    void togglingEnabled_doesNotCountWhileDisabled_thenCountsWhenEnabled() {
        RateLimiterService svc = new RateLimiterService();
        // Start disabled; calls should always be allowed and not counted
        ReflectionTestUtils.setField(svc, "enabled", false);
        ReflectionTestUtils.setField(svc, "windowMs", 100L);
        ReflectionTestUtils.setField(svc, "maxRequests", 1);

        String key = "toggle-key";
        assertTrue(svc.allow(key));
        assertTrue(svc.allow(key));
        assertTrue(svc.allow(key));

        // Enable and verify counting starts from a fresh window
        ReflectionTestUtils.setField(svc, "enabled", true);

        assertTrue(svc.allow(key), "First counted request after enabling should be allowed");
        assertFalse(svc.allow(key), "Second counted request within window should be blocked when maxRequests=1");
    }
}
