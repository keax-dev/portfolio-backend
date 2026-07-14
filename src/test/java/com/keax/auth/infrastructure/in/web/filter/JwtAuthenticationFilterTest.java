package com.keax.auth.infrastructure.in.web.filter;

import com.keax.auth.infrastructure.out.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica el filtro JWT de forma aislada para cabecera ausente, token inválido,
 * autenticación válida y subject cuyo usuario ya no existe.
 */
class JwtAuthenticationFilterTest {

    @AfterEach
    void clearSecurityContext() {
        // Evita que una autenticación contamine la siguiente prueba.
        SecurityContextHolder.clearContext();
    }

    @Test
    void continuesWithoutAuthenticationWhenHeaderIsMissing() throws Exception {
        // Arrange: la petición no contiene Authorization.
        Fixture fixture = fixture();
        when(fixture.request.getHeader("Authorization")).thenReturn(null);

        // Act: el filtro procesa la solicitud.
        fixture.filter.doFilterInternal(fixture.request, fixture.response, fixture.chain);

        // Assert: no consulta JWT y siempre continúa la cadena.
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(fixture.jwtUtil, never()).validateToken(org.mockito.ArgumentMatchers.any());
        verify(fixture.chain).doFilter(fixture.request, fixture.response);
    }

    @Test
    void ignoresInvalidBearerToken() throws Exception {
        // Arrange: existe Bearer, pero su firma o expiración no es válida.
        Fixture fixture = fixture();
        when(fixture.request.getHeader("Authorization")).thenReturn("Bearer invalid");
        when(fixture.jwtUtil.validateToken("invalid")).thenReturn(false);

        // Act: se procesa la solicitud.
        fixture.filter.doFilterInternal(fixture.request, fixture.response, fixture.chain);

        // Assert: no se carga usuario ni se autentica.
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(fixture.userDetailsService, never()).loadUserByUsername(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void authenticatesValidToken() throws Exception {
        // Arrange: token válido y usuario existente.
        Fixture fixture = fixture();
        when(fixture.request.getHeader("Authorization")).thenReturn("Bearer valid");
        when(fixture.jwtUtil.validateToken("valid")).thenReturn(true);
        when(fixture.jwtUtil.extractUsername("valid")).thenReturn("admin");
        when(fixture.userDetailsService.loadUserByUsername("admin"))
                .thenReturn(new User("admin", "password", List.of()));

        // Act: el filtro valida y carga el principal.
        fixture.filter.doFilterInternal(fixture.request, fixture.response, fixture.chain);

        // Assert: el contexto contiene al usuario autenticado.
        assertEquals(
                "admin",
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        verify(fixture.chain).doFilter(fixture.request, fixture.response);
    }

    @Test
    void clearsContextWhenTokenUserNoLongerExists() throws Exception {
        // Arrange: el JWT es válido, pero el usuario fue eliminado.
        Fixture fixture = fixture();
        when(fixture.request.getHeader("Authorization")).thenReturn("Bearer valid");
        when(fixture.jwtUtil.validateToken("valid")).thenReturn(true);
        when(fixture.jwtUtil.extractUsername("valid")).thenReturn("deleted-user");
        when(fixture.userDetailsService.loadUserByUsername("deleted-user"))
                .thenThrow(new UsernameNotFoundException("missing"));

        // Act: se procesa el token huérfano.
        fixture.filter.doFilterInternal(fixture.request, fixture.response, fixture.chain);

        // Assert: no queda autenticación residual y la cadena continúa.
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(fixture.chain).doFilter(fixture.request, fixture.response);
    }

    private Fixture fixture() {
        // Agrupa todos los colaboradores necesarios para cada escenario.
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        return new Fixture(
                new JwtAuthenticationFilter(userDetailsService, jwtUtil),
                userDetailsService,
                jwtUtil,
                mock(HttpServletRequest.class),
                mock(HttpServletResponse.class),
                mock(FilterChain.class)
        );
    }

    private record Fixture(
            JwtAuthenticationFilter filter,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) {
    }

}
