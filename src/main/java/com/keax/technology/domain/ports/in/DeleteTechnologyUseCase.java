package com.keax.technology.domain.ports.in;

import com.keax.technology.domain.model.Technology;

public interface DeleteTechnologyUseCase {

    Technology deleteTechnology(Long technologyId);

}
