package com.keax.application.usecases.Technology;

import com.keax.domain.ports.in.Technology.CreateTechnologyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Technology;

@Component
public class CreateTechnologyUseCaseImpl implements CreateTechnologyUseCase {

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Technology createTechnology(Technology technology) {

        technology.setTechnologyName(technology.getTechnologyName().toUpperCase());

        technologyRepositoryPort.findByTechnologyNameAndTechnologyDeleted(technology.getTechnologyName(), false).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already a technology with this name");
                }
        );

        technology.setTechnologyId(null);

        return technologyRepositoryPort.createTechnology(technology);
    }

}
