package com.keax.domain.ports.in.Technology;

import com.keax.domain.models.Technology;

public interface UpdateTechnologyUseCase {

    Technology updateTechnology(Long technologyId, Technology technology);

}
