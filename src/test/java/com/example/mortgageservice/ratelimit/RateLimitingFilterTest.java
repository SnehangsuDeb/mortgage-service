package com.example.mortgageservice.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RateLimitingFilterTest {

    @Test
    void whenLimiterDisabled_requestsPassThrough() throws ServletException, IOException {
        RateLimiterService limiter = mock(RateLimiterService.class);
        when(limiter.isEnabled()).thenReturn(false);

        RateLimitingFilter filter = new RateLimitingFilter(limiter);

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/test");
        req.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse res = new MockHttpServletResponse();

        AtomicBoolean chainInvoked = new AtomicBoolean(false);
        FilterChain chain = (request, response) -> chainInvoked.set(true);

        filter.doFilter(req, res, chain);

        assertTrue(chainInvoked.get(), "Filter chain should have been invoked when limiter disabled");
        assertEquals(200, res.getStatus(), "Default status should remain 200");
        assertEquals("", res.getContentAsString());
    }

    @Test
    void whenAllowed_requestPassesThrough() throws ServletException, IOException {
        RateLimiterService limiter = mock(RateLimiterService.class);
        when(limiter.isEnabled()).thenReturn(true);
        when(limiter.allow(anyString())).thenReturn(true);

        RateLimitingFilter filter = new RateLimitingFilter(limiter);

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/test");
        req.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse res = new MockHttpServletResponse();

        AtomicBoolean chainInvoked = new AtomicBoolean(false);
        FilterChain chain = (request, response) -> chainInvoked.set(true);

        filter.doFilter(req, res, chain);

        assertTrue(chainInvoked.get(), "Filter chain should have been invoked when request allowed");
        assertEquals(200, res.getStatus(), "Default status should remain 200");
        assertEquals("", res.getContentAsString());
        verify(limiter, times(1)).allow(anyString());
    }

    @Test
    void whenLimited_returns429WithJsonBody() throws ServletException, IOException {
        RateLimiterService limiter = mock(RateLimiterService.class);
        when(limiter.isEnabled()).thenReturn(true);
        when(limiter.allow(anyString())).thenReturn(false);

        RateLimitingFilter filter = new RateLimitingFilter(limiter);

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/test");
        req.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse res = new MockHttpServletResponse();

        FilterChain chain = (request, response) -> fail("Chain must not be invoked when limited");

        filter.doFilter(req, res, chain);

        assertEquals(429, res.getStatus());
        String body = res.getContentAsString();
        assertNotNull(body);
        assertTrue(body.contains("\"status\":429"));
        assertTrue(body.contains("\"message\":\"Rate limit exceeded"));
        assertTrue(body.contains("\"path\":\"/api/test\""));
        assertNotNull(res.getContentType());
        assertTrue(res.getContentType().contains("application/json"));
        verify(limiter, times(1)).allow(anyString());
    }
}
