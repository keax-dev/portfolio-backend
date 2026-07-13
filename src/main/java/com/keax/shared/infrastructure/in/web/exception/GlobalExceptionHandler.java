package com.keax.shared.infrastructure.in.web.exception;

import com.keax.shared.domain.exceptions.AuthenticationFailedException;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.RateLimitExceededException;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import com.keax.shared.infrastructure.in.web.filter.RequestCorrelationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<String> messages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.toList());

        logClientError(HttpStatus.BAD_REQUEST, request, "Validation error", messages, null);
        return errorResponse(HttpStatus.BAD_REQUEST, "Validation error", messages);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        List<String> messages = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .toList();

        logClientError(HttpStatus.BAD_REQUEST, request, "Validation error", messages, null);
        return errorResponse(HttpStatus.BAD_REQUEST, "Validation error", messages);
    }

    @ExceptionHandler(ExceptionAlert.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleExceptionAlert(
            ExceptionAlert ex,
            HttpServletRequest request
    ) {
        logClientError(HttpStatus.BAD_REQUEST, request, ex.getMessage(), List.of(), null);
        return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of());
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleAuthenticationFailed(
            AuthenticationFailedException ex,
            HttpServletRequest request
    ) {
        logClientError(HttpStatus.UNAUTHORIZED, request, ex.getMessage(), List.of(), null);
        return errorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), List.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        List<String> messages = List.of("You do not have permission to access this resource");
        logClientError(HttpStatus.FORBIDDEN, request, "Access denied", messages, null);
        return errorResponse(HttpStatus.FORBIDDEN, "Access denied", messages);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        logClientError(HttpStatus.NOT_FOUND, request, ex.getMessage(), List.of(), null);
        return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), List.of());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceConflict(
            ResourceConflictException ex,
            HttpServletRequest request
    ) {
        logClientError(HttpStatus.CONFLICT, request, ex.getMessage(), List.of(), null);
        return errorResponse(HttpStatus.CONFLICT, ex.getMessage(), List.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        List<String> messages = List.of("The requested operation conflicts with persisted data");
        logClientError(HttpStatus.CONFLICT, request, "Data integrity error", messages, ex);
        return errorResponse(HttpStatus.CONFLICT, "Data integrity error", messages);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleRateLimitExceeded(
            RateLimitExceededException ex,
            HttpServletRequest request
    ) {
        logClientError(HttpStatus.TOO_MANY_REQUESTS, request, ex.getMessage(), List.of(), null);
        return errorResponse(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(), List.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleMalformedBody(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        List<String> messages = List.of("The request body is malformed or contains invalid values");
        logClientError(HttpStatus.BAD_REQUEST, request, "Malformed request body", messages, ex);
        return errorResponse(HttpStatus.BAD_REQUEST, "Malformed request body", messages);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        List<String> messages = List.of("The parameter '%s' has an invalid value".formatted(ex.getName()));
        logClientError(HttpStatus.BAD_REQUEST, request, "Invalid request parameter", messages, ex);
        return errorResponse(HttpStatus.BAD_REQUEST, "Invalid request parameter", messages);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request
    ) {
        List<String> messages = List.of("The required parameter '%s' is missing".formatted(ex.getParameterName()));
        logClientError(HttpStatus.BAD_REQUEST, request, "Missing request parameter", messages, null);
        return errorResponse(HttpStatus.BAD_REQUEST, "Missing request parameter", messages);
    }

    @ExceptionHandler(ExceptionMessage.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleExceptionMessage(
            ExceptionMessage ex,
            HttpServletRequest request
    ) {
        List<String> messages = List.of(ex.getMessage());
        logClientError(HttpStatus.BAD_REQUEST, request, "An error has occurred", messages, null);
        return errorResponse(HttpStatus.BAD_REQUEST, "An error has occurred", messages);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleExternalService(
            ExternalServiceException ex,
            HttpServletRequest request
    ) {
        logServerError(HttpStatus.BAD_GATEWAY, request, ex.getMessage(), ex);
        return errorResponse(HttpStatus.BAD_GATEWAY, ex.getMessage(), List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request
    ) {
        List<String> messages = List.of("Please try again later");
        logServerError(HttpStatus.INTERNAL_SERVER_ERROR, request, "An unexpected error has occurred", ex);
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error has occurred", messages);
    }

    private ResponseEntity<ApiResponseDTO<Object>> errorResponse(
            HttpStatusCode status,
            String alert,
            List<String> messages
    ) {
        return ResponseEntity.status(status).body(new ApiResponseDTO<>(
                false,
                alert,
                messages,
                null
        ));
    }

    private void logClientError(
            HttpStatus status,
            HttpServletRequest request,
            String alert,
            List<String> messages,
            Exception ex
    ) {
        if (ex == null) {
            log.warn(
                    "Handled client error. status={} method={} path={} requestId={} alert={} messages={}",
                    status.value(),
                    request.getMethod(),
                    request.getRequestURI(),
                    resolveRequestId(request),
                    alert,
                    messages
            );
            return;
        }

        log.warn(
                "Handled client error. status={} method={} path={} requestId={} alert={} messages={}",
                status.value(),
                request.getMethod(),
                request.getRequestURI(),
                resolveRequestId(request),
                alert,
                messages,
                ex
        );
    }

    private void logServerError(
            HttpStatus status,
            HttpServletRequest request,
            String alert,
            Exception ex
    ) {
        log.error(
                "Handled server error. status={} method={} path={} requestId={} alert={}",
                status.value(),
                request.getMethod(),
                request.getRequestURI(),
                resolveRequestId(request),
                alert,
                ex
        );
    }

    private String resolveRequestId(HttpServletRequest request) {
        Object requestId = request.getAttribute(RequestCorrelationFilter.REQUEST_ID_ATTRIBUTE);
        return requestId == null ? "n/a" : requestId.toString();
    }

}
