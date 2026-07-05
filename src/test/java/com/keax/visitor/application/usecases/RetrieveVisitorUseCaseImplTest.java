package com.keax.visitor.application.usecases;

import com.keax.visitor.domain.model.VisitorCityCount;
import com.keax.visitor.domain.model.VisitorCountryCount;
import com.keax.visitor.domain.model.VisitorDashboard;
import com.keax.visitor.domain.ports.out.VisitorRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica que el dashboard de visitantes combine correctamente las metricas
 * del puerto de persistencia y calcule las ultimas 24 horas con un reloj fijo.
 */
class RetrieveVisitorUseCaseImplTest {

    @Test
    void buildsDashboardFromRepositoryMetrics() {
        // Arrange: se preparan metricas independientes para el mismo rango.
        Instant now = Instant.parse("2026-07-05T12:00:00Z");
        Instant startAt = now.minusSeconds(7 * 24 * 60 * 60);
        VisitorRepositoryPort repository = mock(VisitorRepositoryPort.class);
        when(repository.countVisitors(startAt, now)).thenReturn(12L);
        when(repository.countUniqueVisitorIps(startAt, now)).thenReturn(7L);
        when(repository.countByVisitorVisitedAtAfter(now.minusSeconds(24 * 60 * 60))).thenReturn(3L);
        when(repository.countByCountry(startAt, now))
                .thenReturn(List.of(new VisitorCountryCount("Ecuador", 8L)));
        when(repository.countByCity(startAt, now))
                .thenReturn(List.of(new VisitorCityCount("Quito", 5L)));
        RetrieveVisitorUseCaseImpl useCase = new RetrieveVisitorUseCaseImpl(
                repository,
                Clock.fixed(now, ZoneOffset.UTC)
        );

        // Act: se solicita el dashboard.
        VisitorDashboard dashboard = useCase.getDashboard(startAt, now);

        // Assert: cada valor proviene de la consulta correspondiente.
        assertEquals(12L, dashboard.getTotalVisits());
        assertEquals(7L, dashboard.getUniqueVisitors());
        assertEquals(3L, dashboard.getVisitsLast24Hours());
        assertEquals("Ecuador", dashboard.getCountries().getFirst().getCountry());
        assertEquals("Quito", dashboard.getCities().getFirst().getCity());
        verify(repository).countByVisitorVisitedAtAfter(now.minusSeconds(24 * 60 * 60));
    }

}
