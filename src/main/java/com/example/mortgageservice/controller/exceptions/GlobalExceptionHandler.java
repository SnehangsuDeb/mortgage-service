package com.example.mortgageservice.controller.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + (fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid"))
                .collect(Collectors.joining(", "));
        if (details.isEmpty()) {
            details = ex.getBindingResult().getAllErrors().stream()
                    .map(err -> err.getObjectName() + ": " + (err.getDefaultMessage() != null ? err.getDefaultMessage() : "invalid"))
                    .collect(Collectors.joining(", "));
        }
        log.warn("Validation failed: {} - path: {}", details, req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, details, req, ex.getCause());
    }

    // 400 - Constraint violations for query/path params
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        String details = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));
        log.warn("Constraint violation: {} - path: {}", details, req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, details, req, ex.getCause());
    }

    // 400 - Binding errors (e.g., query params, form data)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleBindException(BindException ex, HttpServletRequest req) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        if (details.isEmpty()) {
            details = "Invalid request parameters";
        }
        log.warn("Bind exception: {} - path: {}", details, req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, details, req, ex.getCause());
    }

    // 400 - Malformed JSON or unreadable body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "Malformed JSON request";
        log.warn("Unreadable HTTP message: {} - path: {}", msg, req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, msg, req, ex.getCause());
    }

    // 400 - Missing required request parameter
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        String msg = "Missing required parameter: " + ex.getParameterName();
        log.warn("Missing parameter: {} - path: {}", msg, req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, msg, req, ex.getCause());
    }

    // 400 - Argument type mismatch (e.g., invalid enum, number format)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String required = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "required type";
        String msg = "Parameter '" + ex.getName() + "' should be of type " + required;
        log.warn("Type mismatch: {} - path: {}", msg, req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, msg, req, ex.getCause());
    }

    // 404 - Entity not found or no element present
    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex, HttpServletRequest req) {
        log.warn("Not found: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req, ex.getCause());
    }

    // 405 - Method not allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        log.warn("Method not allowed: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), req, ex.getCause());
    }

    // 406 - Not acceptable
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiError> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpServletRequest req) {
        log.warn("Not acceptable: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), req, ex.getCause());
    }

    // 415 - Unsupported media type
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        log.warn("Unsupported media type: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), req, ex.getCause());
    }

    // 409 - Conflicts like unique constraint violations or illegal state
    @ExceptionHandler({DataIntegrityViolationException.class, IllegalStateException.class})
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex, HttpServletRequest req) {
        log.warn("Conflict: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.CONFLICT, ex.getMessage(), req, ex.getCause());
    }

    // 400 - Illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        log.warn("Illegal argument: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return toResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req, ex.getCause());
    }

    // 500 - Fallback for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error at {}: ", req.getRequestURI(), ex);
        return toResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", req, ex.getCause());
    }

    private ResponseEntity<ApiError> toResponse(HttpStatus status, String message, HttpServletRequest req, Throwable cause) {
        ApiError body = ApiError.builder().timestamp(OffsetDateTime.now())
                .status(status.value())
                .cause(status.getReasonPhrase())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
