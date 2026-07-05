package com.keax.auth.application.usecases;

import com.keax.auth.domain.model.Auth;
import com.keax.auth.domain.ports.out.AuthenticationPort;
import com.keax.auth.domain.ports.out.TokenProviderPort;
import com.keax.shared.domain.exceptions.AuthenticationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Verifica que el caso de uso de login coordine la autenticacion y emita un
 * token solo cuando las credenciales sean validas.
 */
@ExtendWith(MockitoExtension.class)
class AuthLoginUseCaseImplTest {

    @Mock
    private AuthenticationPort authenticationPort;

    @Mock
    private TokenProviderPort tokenProviderPort;

    private AuthLoginUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        // Construye el caso de uso solo con puertos simulados.
        useCase = new AuthLoginUseCaseImpl(authenticationPort, tokenProviderPort);
    }

    @Test
    void returnsAuthenticatedModelWithGeneratedToken() {
        // Arrange: las credenciales son validas y el proveedor emite un token.
        Auth auth = new Auth("admin", "secret", null);
        when(authenticationPort.authenticate("admin", "secret")).thenReturn(true);
        when(tokenProviderPort.generateToken("admin")).thenReturn("signed-token");

        // Act: se ejecuta el flujo de login.
        Auth result = useCase.login(auth);

        // Assert: conserva el modelo y agrega el token generado.
        assertSame(auth, result);
        assertEquals("signed-token", result.getToken());
    }

    @Test
    void rejectsInvalidCredentialsWithoutGeneratingToken() {
        // Arrange: el adaptador de autenticacion rechaza las credenciales.
        Auth auth = new Auth("admin", "wrong", null);
        when(authenticationPort.authenticate("admin", "wrong")).thenReturn(false);

        // Act y Assert: se informa un fallo y nunca se intenta firmar un token.
        assertThrows(AuthenticationFailedException.class, () -> useCase.login(auth));
        verifyNoInteractions(tokenProviderPort);
    }

}
