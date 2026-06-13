package com.keax.project.application.usecases;

import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.project.domain.ports.in.CreateProjectUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;

@Service
@Transactional
public class CreateProjectUseCaseImpl implements CreateProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Project createProject(Project project) {

        technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(
                project.getTechnologyId(),
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The technology entered was not found")
        );

        project.setProjectTitle(project.getProjectTitle().toUpperCase());
        projectRepositoryPort.findByProjectTitleAndProjectDeleted(
                project.getProjectTitle(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a project with this title");
                }
        );

        projectRepositoryPort.findByProjectPositionAndProjectDeletedAndTechnology_technologyId(
                project.getProjectPosition(),
                false,
                project.getTechnologyId()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("The project position is already filled");
                }
        );

        project.setProjectTitleEs(project.getProjectTitleEs().toUpperCase());

        project.setProjectId(null);
        project.setProjectPicture(null);
        project.setProjectDeleted(false);

        return projectRepositoryPort.createProject(project);
    }

}
