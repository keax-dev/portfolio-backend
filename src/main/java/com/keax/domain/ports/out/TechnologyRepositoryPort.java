package com.keax.domain.ports.out;

import com.keax.domain.models.Technology;
import java.util.Optional;
import java.util.List;

public interface TechnologyRepositoryPort {

    Technology createTechnology(Technology technology);
    Technology updateTechnology(Technology technology);
    Technology deleteTechnology(Technology technology);
    List<Technology> findByTechnologyDeleted(Boolean deleted);
    List<Technology> getListTechnology();
    Optional<Technology> findByTechnologyNameAndTechnologyDeleted(String technologyName, Boolean deleted);
    Optional<Technology> findByTechnologyIdAndTechnologyDeleted(Long technologyId, Boolean deleted);
    Optional<Technology> findByTechnologyPositionAndTechnologyDeleted(int position, Boolean deleted);

}
