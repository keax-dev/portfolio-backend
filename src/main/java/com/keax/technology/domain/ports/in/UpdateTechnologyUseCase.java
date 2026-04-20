package com.keax.technology.domain.ports.in;

import com.keax.technology.domain.model.Technology;

public interface UpdateTechnologyUseCase {

    Technology updateTechnology(Long technologyId, Technology technology);

}
