package com.keax.application.usecases.Technology;

import com.keax.domain.ports.in.Technology.DeleteTechnologyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Technology;

@Component
public class DeleteTechnologyUseCaseImpl implements DeleteTechnologyUseCase {

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Technology deleteTechnology(Long technologyId) {

        Technology technology = technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(technologyId, false).orElseThrow(
                () -> new ExceptionAlert("The technology entered was not found")
        );

        technology.setTechnologyDeleted(true);

        return technologyRepositoryPort.deleteTechnology(technology);
    }

}
