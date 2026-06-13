package com.keax.shared.infrastructure.in.web.exception;

import com.keax.shared.domain.exceptions.AuthenticationFailedException;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsAuthenticationFailureToUnauthorized() {
        var response = handler.handleAuthenticationFailed(new AuthenticationFailedException("Invalid login"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void mapsResourceNotFoundToNotFound() {
        var response = handler.handleResourceNotFound(new ResourceNotFoundException("Not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void mapsResourceConflictToConflict() {
        var response = handler.handleResourceConflict(new ResourceConflictException("Conflict"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void mapsExternalServiceFailureToBadGateway() {
        var response = handler.handleExternalService(new ExternalServiceException("External failure"));

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

}
