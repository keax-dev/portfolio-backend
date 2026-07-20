package com.keax.visitor.infrastructure.in.web.resolver;

import com.keax.shared.infrastructure.in.web.client.ClientIpResolver;
import com.keax.shared.infrastructure.in.web.client.ClientIdentityHasher;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Comprueba que las cabeceras reenviadas solo se acepten cuando exista confianza
 * explicita en el proxy y que, por defecto, se use la direccion de la conexion.
 */
class ClientIpResolverTest {

    @Test
    void ignoresSpoofableHeadersWhenProxyTrustIsDisabled() {
        // Arrange: un cliente intenta enviar una IP arbitraria en X-Forwarded-For.
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("10.0.0.5");
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.20");
        ClientIpResolver resolver = new ClientIpResolver(false);

        // Act y Assert: se conserva la IP de la conexion confiable.
        assertEquals("10.0.0.5", resolver.resolve(request));
    }

    @Test
    void usesFirstForwardedIpWhenTrustedProxyIsEnabled() {
        // Arrange: el proxy confiable aporta una cadena de saltos.
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For"))
                .thenReturn("203.0.113.20, 10.0.0.5");
        ClientIpResolver resolver = new ClientIpResolver(true);

        // Act y Assert: se toma la direccion original, no la del proxy intermedio.
        assertEquals("203.0.113.20", resolver.resolve(request));
    }

    @Test
    void prioritizesCloudflareConnectingIpForTrustedProxy() {
        // Arrange: Cloudflare envia su cabecera especifica y tambien X-Forwarded-For.
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("CF-Connecting-IP")).thenReturn("198.51.100.7");
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.20");
        ClientIpResolver resolver = new ClientIpResolver(true);

        // Act y Assert: se respeta la prioridad declarada por el adaptador.
        assertEquals("198.51.100.7", resolver.resolve(request));
    }

    @Test
    void acceptsForwardedHeadersOnlyFromAnExplicitlyTrustedProxy() {
        HttpServletRequest trustedRequest = mock(HttpServletRequest.class);
        when(trustedRequest.getRemoteAddr()).thenReturn("10.0.0.5");
        when(trustedRequest.getHeader("X-Forwarded-For")).thenReturn("203.0.113.20");
        HttpServletRequest untrustedRequest = mock(HttpServletRequest.class);
        when(untrustedRequest.getRemoteAddr()).thenReturn("10.0.0.6");
        when(untrustedRequest.getHeader("X-Forwarded-For")).thenReturn("203.0.113.30");
        ClientIpResolver resolver = new ClientIpResolver(true, "10.0.0.5");

        assertEquals("203.0.113.20", resolver.resolve(trustedRequest));
        assertEquals("10.0.0.6", resolver.resolve(untrustedRequest));
    }

    @Test
    void hashesClientIdentityDeterministicallyWithoutPersistingTheRawIp() {
        ClientIdentityHasher hasher = new ClientIdentityHasher(
                "0123456789abcdef0123456789abcdef"
        );

        String firstHash = hasher.hash("203.0.113.20");

        assertEquals(firstHash, hasher.hash("203.0.113.20"));
        assertEquals(64, firstHash.length());
        assertNotEquals("203.0.113.20", firstHash);
    }

}
