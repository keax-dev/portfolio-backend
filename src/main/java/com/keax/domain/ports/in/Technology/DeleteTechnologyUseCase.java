package com.keax.domain.ports.in.Technology;

import com.keax.domain.models.Technology;

public interface DeleteTechnologyUseCase {

    Technology deleteTechnology(Long technologyId);

}
