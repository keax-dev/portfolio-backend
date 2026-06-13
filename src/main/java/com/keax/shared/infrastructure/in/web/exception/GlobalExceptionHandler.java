package com.keax.shared.infrastructure.in.web.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import com.keax.shared.domain.exceptions.RateLimitExceededException;
import com.keax.shared.domain.exceptions.AuthenticationFailedException;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.stream.Collectors;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<String> messages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                }).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(new ApiResponseDTO<>(
                false,
                "Validation error",
                messages,
                null
        ));
    }

    @ExceptionHandler(ExceptionAlert.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleExceptionAlert(ExceptionAlert ex) {
        return ResponseEntity.badRequest().body(new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleAuthenticationFailed(AuthenticationFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceConflict(ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleExternalService(ExternalServiceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(ExceptionMessage.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleExceptionMessage(ExceptionMessage ex) {
        return ResponseEntity.badRequest().body(new ApiResponseDTO<>(
                false,
                "An error has occurred",
                List.of(ex.getMessage()),
                null
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDTO<>(
                false,
                "Data integrity error",
                List.of("The requested operation conflicts with persisted data"),
                null
        ));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleRateLimitExceeded(RateLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleUnexpectedException(Exception ex) {
        log.error("Unexpected application error", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<>(
                false,
                "An unexpected error has occurred",
                List.of("Please try again later"),
                null
        ));
    }

}
