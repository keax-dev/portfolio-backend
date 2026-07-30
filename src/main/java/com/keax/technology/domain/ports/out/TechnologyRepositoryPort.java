package com.keax.technology.domain.ports.out;

import com.keax.technology.domain.model.Technology;
import java.util.Optional;
import java.util.List;

public interface TechnologyRepositoryPort {

    Technology createTechnology(Technology technology);
    Technology updateTechnology(Technology technology);
    Technology deleteTechnology(Technology technology);
    List<Technology> findAll();
    Optional<Technology> findByName(String technologyName);
    Optional<Technology> findById(Long technologyId);

}
