package com.keax.shared.infrastructure.in.web.exception;

import com.keax.shared.domain.exceptions.AuthenticationFailedException;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifica el contrato HTTP que el manejador global asigna a las excepciones
 * principales del dominio y de servicios externos.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsAuthenticationFailureToUnauthorized() {
        // Act: se transforma un fallo de credenciales.
        var response = handler.handleAuthenticationFailed(new AuthenticationFailedException("Invalid login"));

        // Assert: autenticacion invalida corresponde a HTTP 401.
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void mapsResourceNotFoundToNotFound() {
        // Act: se transforma un recurso inexistente.
        var response = handler.handleResourceNotFound(new ResourceNotFoundException("Not found"));

        // Assert: el recurso ausente corresponde a HTTP 404.
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void mapsResourceConflictToConflict() {
        // Act: se transforma un conflicto de reglas o persistencia.
        var response = handler.handleResourceConflict(new ResourceConflictException("Conflict"));

        // Assert: el conflicto corresponde a HTTP 409.
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void mapsExternalServiceFailureToBadGateway() {
        // Act: se transforma un fallo de una integracion saliente.
        var response = handler.handleExternalService(new ExternalServiceException("External failure"));

        // Assert: la dependencia externa fallida corresponde a HTTP 502.
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

}
