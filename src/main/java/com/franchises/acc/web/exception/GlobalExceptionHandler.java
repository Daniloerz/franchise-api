package com.franchises.acc.web.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.franchises.acc.domain.exception.DuplicateResourceException;
import com.franchises.acc.domain.exception.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse body = buildErrorResponse(status, ex.getMessage(), extractPath(request));
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse body = buildErrorResponse(status, ex.getMessage(), extractPath(request));
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                      WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        ErrorResponse body = buildErrorResponse(status, validationErrors, extractPath(request));
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                   WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse body = buildErrorResponse(status, message, extractPath(request));
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unexpected error", ex);

        ErrorResponse body = buildErrorResponse(status, "An unexpected error occurred", extractPath(request));
        return new ResponseEntity<>(body, status);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }

    private String extractPath(WebRequest request) {
        String description = request.getDescription(false); // e.g. "uri=/api/franchises"
        if (description != null && description.startsWith("uri=")) {
            return description.substring(4);
        }
        return description;
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}

