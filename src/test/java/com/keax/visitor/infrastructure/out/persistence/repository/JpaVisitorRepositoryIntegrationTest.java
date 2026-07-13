package com.keax.visitor.infrastructure.out.persistence.repository;

import com.keax.visitor.infrastructure.out.persistence.entity.VisitorEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integra Spring Data JPA con H2 en modo PostgreSQL para verificar el rango,
 * visitantes unicos y agregaciones geograficas del repositorio de visitas.
 */
@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JpaVisitorRepositoryIntegrationTest {

    private final JpaVisitorRepository repository;

    JpaVisitorRepositoryIntegrationTest(JpaVisitorRepository repository) {
        this.repository = repository;
    }

    @Test
    void aggregatesVisitorMetricsInsideRequestedRange() {
        // Arrange: se guardan tres visitas en el rango y una fuera de el.
        Instant startAt = Instant.parse("2026-07-01T00:00:00Z");
        Instant endAt = Instant.parse("2026-07-08T00:00:00Z");
        repository.save(visitor("203.0.113.1", "Ecuador", "Quito", startAt.plusSeconds(60)));
        repository.save(visitor("203.0.113.1", "Ecuador", "Quito", startAt.plusSeconds(120)));
        repository.save(visitor("203.0.113.2", "Colombia", "Bogota", startAt.plusSeconds(180)));
        repository.save(visitor("203.0.113.3", "Peru", "Lima", startAt.minusSeconds(60)));
        repository.flush();

        // Act: se consultan las metricas que alimentan el dashboard.
        long total = repository.countByVisitorVisitedAtBetween(startAt, endAt);
        long unique = repository.countUniqueVisitorIps(startAt, endAt);
        var countries = repository.countByCountry(startAt, endAt);
        var cities = repository.countByCity(startAt, endAt);
        var orderedVisits = repository.findByVisitorVisitedAtBetweenOrderByVisitorVisitedAtDesc(
                startAt,
                endAt
        );

        // Assert: el rango, distinct, agrupacion y orden se ejecutan en persistencia.
        assertEquals(3L, total);
        assertEquals(2L, unique);
        assertEquals("Ecuador", countries.getFirst().getCountry());
        assertEquals(2L, countries.getFirst().getTotal());
        assertEquals("Quito", cities.getFirst().getCity());
        assertEquals(2L, cities.getFirst().getTotal());
        assertEquals("203.0.113.2", orderedVisits.getFirst().getVisitorIp());
    }

    private VisitorEntity visitor(String ip, String country, String city, Instant visitedAt) {
        // Construye una entidad completa para ejercitar el mapeo de columnas real.
        return new VisitorEntity(
                null,
                ip,
                country,
                city,
                "JUnit",
                "/portfolio",
                visitedAt
        );
    }

}
