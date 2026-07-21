package com.keax.visitor.application.usecases;

import com.keax.visitor.domain.model.Visitor;
import com.keax.visitor.domain.ports.out.VisitorRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica la ventana de deduplicacion del registro de visitantes y que cada
 * visita persistida reciba una fecha controlada.
 */
class RegisterVisitorUseCaseImplTest {

    private static final Instant NOW = Instant.parse("2026-07-05T12:00:00Z");

    @Test
    void savesVisitorOutsideDeduplicationWindow() {
        // Arrange: la visita anterior queda fuera de la ventana de treinta minutos.
        VisitorRepositoryPort repository = mock(VisitorRepositoryPort.class);
        Visitor previous = visitorAt(NOW.minusSeconds(31 * 60));
        Visitor incoming = visitorAt(null);
        when(repository.findLatestByVisitorIp("203.0.113.10")).thenReturn(Optional.of(previous));
        when(repository.saveVisitor(any(Visitor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RegisterVisitorUseCaseImpl useCase = useCase(repository, 30);

        // Act: se intenta registrar la visita actual.
        Optional<Visitor> result = useCase.registerVisitor(incoming);

        // Assert: la visita se guarda con la hora del reloj controlado.
        assertTrue(result.isPresent());
        assertEquals(NOW, result.orElseThrow().getVisitorVisitedAt());
        verify(repository).saveVisitor(incoming);
    }

    @Test
    void ignoresVisitorInsideDeduplicationWindow() {
        // Arrange: existe una visita de la misma IP hace diez minutos.
        VisitorRepositoryPort repository = mock(VisitorRepositoryPort.class);
        when(repository.findLatestByVisitorIp("203.0.113.10"))
                .thenReturn(Optional.of(visitorAt(NOW.minusSeconds(10 * 60))));
        RegisterVisitorUseCaseImpl useCase = useCase(repository, 30);

        // Act: se procesa una nueva visita.
        Optional<Visitor> result = useCase.registerVisitor(visitorAt(null));

        // Assert: no se crea un duplicado reciente.
        assertTrue(result.isEmpty());
        verify(repository, never()).saveVisitor(any());
    }

    @Test
    void treatsExactWindowBoundaryAsDuplicate() {
        // Arrange: la ultima visita coincide exactamente con el limite configurado.
        VisitorRepositoryPort repository = mock(VisitorRepositoryPort.class);
        when(repository.findLatestByVisitorIp("203.0.113.10"))
                .thenReturn(Optional.of(visitorAt(NOW.minusSeconds(30 * 60))));
        RegisterVisitorUseCaseImpl useCase = useCase(repository, 30);

        // Act y Assert: el limite sigue perteneciendo a la ventana de deduplicacion.
        assertTrue(useCase.registerVisitor(visitorAt(null)).isEmpty());
        verify(repository, never()).saveVisitor(any());
    }

    @Test
    void ignoresOwnerIpRangeBeforeDeduplication() {
        // Arrange: la IP pertenece al rango propio excluido del dashboard.
        VisitorRepositoryPort repository = mock(VisitorRepositoryPort.class);
        RegisterVisitorUseCaseImpl useCase = useCase(repository, 30);
        Visitor incoming = new Visitor(
                null,
                "45.70.58.27",
                "Ecuador",
                "Quito",
                "JUnit",
                "/",
                null
        );

        // Act: se intenta registrar la visita propia.
        Optional<Visitor> result = useCase.registerVisitor(incoming);

        // Assert: no consulta ni persiste datos para esa IP.
        assertTrue(result.isEmpty());
        verify(repository, never()).findLatestByVisitorIp(any());
        verify(repository, never()).saveVisitor(any());
    }

    @Test
    void rejectsNegativeDeduplicationWindow() {
        // Act y Assert: una configuracion negativa falla de forma temprana.
        assertThrows(
                IllegalArgumentException.class,
                () -> useCase(mock(VisitorRepositoryPort.class), -1)
        );
    }

    private RegisterVisitorUseCaseImpl useCase(VisitorRepositoryPort repository, long minutes) {
        // Inyecta un reloj fijo para eliminar flakiness temporal.
        return new RegisterVisitorUseCaseImpl(
                repository,
                minutes,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
    }

    private Visitor visitorAt(Instant visitedAt) {
        // Construye el visitante minimo usado en todos los escenarios.
        return new Visitor(
                null,
                "203.0.113.10",
                "Ecuador",
                "Quito",
                "JUnit",
                "/",
                visitedAt
        );
    }

}
