package com.example.mortgageservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import javax.naming.ServiceUnavailableException;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(BadRequestException ex, HttpServletRequest req) {
        log.warn("Validation failed: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req, ex.getCause());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Bean validation failed: {} - path: {}", message, req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, message, req, ex.getCause());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleBindException(BindException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Bind validation failed: {} - path: {}", message, req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, message, req, ex.getCause());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));
        log.warn("Constraint violation: {} - path: {}", message, req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, message, req, ex.getCause());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.warn("Malformed request: {} - path: {}", ex.getMostSpecificCause().getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", req, ex.getCause());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        log.warn("Not found: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req, ex.getCause());
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(NoContentException ex, HttpServletRequest req) {
        log.warn("NoContent: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.NO_CONTENT, ex.getMessage(), req, ex.getCause());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiError> handleMethodServiceUnavailable(ServiceUnavailableException ex, HttpServletRequest req) {
        log.warn("Service unavailable: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), req, ex.getCause());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error at {}: ", req.getRequestURI(), ex);
        return toResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", req, ex.getCause());
    }

    private ResponseEntity<ApiError> toResponse(HttpStatus status, String message, HttpServletRequest req, Throwable cause) {
        ApiError body = ApiError.builder().timestamp(OffsetDateTime.now())
                .status(status.value())
                .cause(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
