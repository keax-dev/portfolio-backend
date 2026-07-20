package com.keax.technology.application.usecases;

import com.keax.technology.domain.model.Technology;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Verifies the flat public technology catalogue used by project relations. */
class RetrieveTechnologyUseCaseImplTest {

    @Test
    void retrievesActiveTechnologiesWithoutNestedProjects() {
        TechnologyRepositoryPort repository = mock(TechnologyRepositoryPort.class);
        Technology technology = new Technology(1L, "JAVA", false);
        when(repository.findByTechnologyDeleted(false)).thenReturn(List.of(technology));

        List<Technology> result = new RetrieveTechnologyUseCaseImpl(repository)
                .findByTechnologyDeleted(false);

        assertEquals(List.of(technology), result);
        assertEquals("JAVA", result.getFirst().getTechnologyName());
    }

    @Test
    void returnsAnEmptyActiveTechnologyCatalogue() {
        TechnologyRepositoryPort repository = mock(TechnologyRepositoryPort.class);
        when(repository.findByTechnologyDeleted(false)).thenReturn(List.of());

        assertTrue(new RetrieveTechnologyUseCaseImpl(repository)
                .findByTechnologyDeleted(false)
                .isEmpty());
    }
}
