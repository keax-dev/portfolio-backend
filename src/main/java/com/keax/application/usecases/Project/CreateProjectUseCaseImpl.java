package com.keax.application.usecases.Project;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Project.CreateProjectUseCase;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.domain.ports.out.ProjectRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Project;

@Component
public class CreateProjectUseCaseImpl implements CreateProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Project createProject(Project project) {

        technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(project.getTechnologyId(), false).orElseThrow(
                () -> new ExceptionAlert("The technology entered was not found")
        );

        project.setProjectTitle(project.getProjectTitle().toUpperCase());

        projectRepositoryPort.findByProjectTitleAndProjectDeleted(project.getProjectTitle(), false).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already a project with this title");
                }
        );

        projectRepositoryPort.findByProjectPositionAndProjectDeletedAndTechnology_technologyId(project.getProjectPosition(), false, project.getTechnologyId()).ifPresent(
                e -> {
                    throw new ExceptionAlert("The project position is already filled");
                }
        );

        project.setProjectTitleEs(project.getProjectTitleEs().toUpperCase());
        project.setProjectDescription(project.getProjectDescription().toUpperCase());
        project.setProjectDescriptionEs(project.getProjectDescriptionEs().toUpperCase());
        project.setProjectPicture(null);
        project.setProjectId(null);

        return projectRepositoryPort.createProject(project);
    }

}
