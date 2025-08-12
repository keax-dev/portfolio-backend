package com.keax.infrastructure.config;

import com.keax.domain.exceptions.ExceptionGlobal;
import com.keax.domain.models.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

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
                "Error de validación",
                messages,
                null
        ));
    }

    @ExceptionHandler(ExceptionGlobal.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateName(ExceptionGlobal ex) {
        return ResponseEntity.ok(new ApiResponse<>(
                false,
                "Se ha producido una excepción.",
                null,
                List.of(ex.getMessage())
        ));
    }

}
