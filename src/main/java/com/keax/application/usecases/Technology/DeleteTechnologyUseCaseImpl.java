package com.keax.application.usecases.Technology;

import com.keax.domain.ports.in.Technology.DeleteTechnologyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.domain.ports.out.ProjectRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Technology;

@Component
public class DeleteTechnologyUseCaseImpl implements DeleteTechnologyUseCase {

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Override
    public Technology deleteTechnology(Long technologyId) {

        Technology technology = technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(technologyId, false).orElseThrow(
                () -> new ExceptionAlert("The technology entered was not found")
        );

        if (projectRepositoryPort.existsByTechnology_technologyIdAndProjectDeleted(technologyId, false)){
            throw new ExceptionAlert("Technology cannot be deleted because it has associated records");
        }


        technology.setTechnologyDeleted(true);

        return technologyRepositoryPort.deleteTechnology(technology);
    }

}
