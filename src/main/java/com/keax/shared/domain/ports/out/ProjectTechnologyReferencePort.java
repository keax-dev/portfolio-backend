package com.keax.shared.domain.ports.out;

import java.util.Set;

public interface ProjectTechnologyReferencePort {

    Set<Long> findActiveTechnologyIds(Set<Long> technologyIds);

    boolean existsActiveProjectForTechnology(Long technologyId);
}
