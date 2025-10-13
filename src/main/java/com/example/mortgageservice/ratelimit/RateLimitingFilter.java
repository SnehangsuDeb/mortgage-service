package com.example.mortgageservice.ratelimit;

import com.example.mortgageservice.exceptions.ApiError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

/**
 * Applies rate limiting to all incoming requests.
 * Key = client IP + path to provide fair per-endpoint throttling.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;

    public RateLimitingFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!rateLimiterService.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = extractClientIp(request);
        String key = clientIp + ":" + request.getRequestURI();

        if (rateLimiterService.allow(key)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Too many requests - respond with JSON ApiError
        HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
        ApiError error = ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .cause(status.getReasonPhrase())
                .message("Rate limit exceeded. Try again later.")
                .path(request.getRequestURI())
                .build();

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String body = String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"cause\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                error.getTimestamp(),
                error.getStatus(),
                escapeJson(error.getCause()),
                escapeJson(error.getMessage()),
                escapeJson(error.getPath())
        );
        response.getWriter().write(body);
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            // Take first IP in the list
            int commaIdx = forwarded.indexOf(',');
            return commaIdx > 0 ? forwarded.substring(0, commaIdx).trim() : forwarded.trim();
        }
        return request.getRemoteAddr();
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
