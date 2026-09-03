package com.stockpilot.exception;

import com.stockpilot.api.dto.ProblemDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("https://api.stockpilot.com/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed for one or more fields");
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("https://api.stockpilot.com/errors/validation-error"));

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ProblemDetailsResponse> handleNoHandlerFound(
            NoHandlerFoundException ex,
            HttpServletRequest request) {

        ProblemDetailsResponse response = new ProblemDetailsResponse(
                "/errors/not-found",
                "Resource Not Found",
                HttpStatus.NOT_FOUND.value(),
                String.format("No route handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL()),
                request.getRequestURI(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetailsResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        ProblemDetailsResponse response = new ProblemDetailsResponse(
                "/errors/method-not-allowed",
                "Method Not Allowed",
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                String.format("Request method '%s' is not supported for this endpoint.", ex.getMethod()),
                request.getRequestURI(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetailsResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        List<ProblemDetailsResponse.InvalidParam> invalidParams = ex.getConstraintViolations().stream()
                .map(violation -> new ProblemDetailsResponse.InvalidParam(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();

        ProblemDetailsResponse response = new ProblemDetailsResponse(
                "/errors/validation-error",
                "Constraint Violation Error",
                HttpStatus.BAD_REQUEST.value(),
                "One or more parameter constraints were violated.",
                request.getRequestURI(),
                Instant.now(),
                invalidParams
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.stockpilot.com/errors/internal-server-error"));
        return problemDetail;
    }
}
