package com.keax.infrastructure.exceptions;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import com.keax.domain.exceptions.ExceptionMessage;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import java.util.stream.Collectors;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<String> messages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {

                    if (error instanceof FieldError fieldError) {
                        return fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();

                }).collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(
                false,
                "Validation error",
                messages,
                null
        ));
    }

    @ExceptionHandler(ExceptionAlert.class)
    public ResponseEntity<ApiResponse<Object>> handleExceptionAlert(ExceptionAlert ex) {
        return ResponseEntity.ok(new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(ExceptionMessage.class)
    public ResponseEntity<ApiResponse<Object>> handleExceptionMessage(ExceptionMessage ex) {
        return ResponseEntity.ok(new ApiResponse<>(
                false,
                "An error has occurred",
                List.of(ex.getMessage()),
                null
        ));
    }

}
