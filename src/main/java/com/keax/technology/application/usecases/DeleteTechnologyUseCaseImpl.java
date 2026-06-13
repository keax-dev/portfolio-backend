package com.keax.technology.application.usecases;

import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import com.keax.technology.domain.ports.in.DeleteTechnologyUseCase;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.technology.domain.model.Technology;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeleteTechnologyUseCaseImpl implements DeleteTechnologyUseCase {

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Override
    public Technology deleteTechnology(Long technologyId) {

        Technology technology = technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(
                technologyId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The technology entered was not found")
        );

        if (projectRepositoryPort.existsByTechnology_technologyIdAndProjectDeleted(
                technologyId,
                false
        )){
            throw new ResourceConflictException("Technology cannot be deleted because it has associated records");
        }

        technology.setTechnologyDeleted(true);

        return technologyRepositoryPort.deleteTechnology(technology);
    }

}
