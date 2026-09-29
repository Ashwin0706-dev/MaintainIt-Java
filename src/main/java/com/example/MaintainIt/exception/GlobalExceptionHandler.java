package com.example.MaintainIt.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        record ErrorResponse(
                        int status,
                        String error,
                        String message,
                        LocalDateTime timestamp) {
        }

        // Handles @NotNull, @NotBlank, @Positive, @Email, etc.
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        MethodArgumentNotValidException ex) {

                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getField()
                                                + ": "
                                                + error.getDefaultMessage())
                                .collect(Collectors.joining("; "));

                return createResponse(
                                HttpStatus.BAD_REQUEST,
                                "Validation failed",
                                message);
        }

        // Handles invalid JSON and wrong data types.
        //
        // Example:
        // maintenanceInterval = "hello"
        //
        // instead of:
        // maintenanceInterval = 100
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleInvalidJson(
                        HttpMessageNotReadableException ex) {

                Throwable cause = ex.getCause();

                while (cause != null) {

                        if (cause instanceof InvalidFormatException) {

                                return createResponse(
                                                HttpStatus.BAD_REQUEST,
                                                "Invalid input",
                                                "One or more fields contain an invalid data type. "
                                                                + "Please enter the correct type of value.");
                        }

                        cause = cause.getCause();
                }

                return createResponse(
                                HttpStatus.BAD_REQUEST,
                                "Invalid request",
                                "Request contains invalid JSON or invalid data.");
        }

        // Handles invalid values in URL parameters.
        //
        // Example:
        // /api/machines/abc
        //
        // when id should be a number.
        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponse> handleTypeMismatch(
                        MethodArgumentTypeMismatchException ex) {

                return createResponse(
                                HttpStatus.BAD_REQUEST,
                                "Invalid input",
                                "Parameter '"
                                                + ex.getName()
                                                + "' contains an invalid value.");
        }

        // Handles resources that do not exist.
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(
                        ResourceNotFoundException ex) {

                return createResponse(
                                HttpStatus.NOT_FOUND,
                                "Not found",
                                ex.getMessage());
        }

        // Handles business rules defined in the service layer.
        @ExceptionHandler(BusinessRuleException.class)
        public ResponseEntity<ErrorResponse> handleBusinessRule(
                        BusinessRuleException ex) {

                return createResponse(
                                HttpStatus.BAD_REQUEST,
                                "Business rule violation",
                                ex.getMessage());
        }

        // Handles database constraint violations.
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDatabaseError(
                        DataIntegrityViolationException ex) {

                return createResponse(
                                HttpStatus.CONFLICT,
                                "Database constraint violation",
                                "The operation conflicts with existing data.");
        }

        // Handles validation constraints.
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolation(
                        ConstraintViolationException ex) {

                return createResponse(
                                HttpStatus.BAD_REQUEST,
                                "Validation failed",
                                ex.getMessage());
        }

        // Handles unexpected errors.
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleOtherErrors(
                        Exception ex) {

                return createResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Internal server error",
                                "An unexpected error occurred.");
        }

        // Creates the common error response.
        private ResponseEntity<ErrorResponse> createResponse(
                        HttpStatus status,
                        String error,
                        String message) {

                ErrorResponse response = new ErrorResponse(
                                status.value(),
                                error,
                                message,
                                LocalDateTime.now());

                return ResponseEntity
                                .status(status)
                                .body(response);
        }
}