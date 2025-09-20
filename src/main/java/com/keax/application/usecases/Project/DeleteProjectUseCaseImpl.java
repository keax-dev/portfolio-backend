package com.keax.application.usecases.Project;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Project.DeleteProjectUseCase;
import com.keax.domain.ports.out.ProjectRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Project;

@Component
public class DeleteProjectUseCaseImpl implements DeleteProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Override
    public Project deleteProject(Long projectId) {

        Project project = projectRepositoryPort.findByProjectIdAndProjectDeleted(projectId, false).orElseThrow(
                () -> new ExceptionAlert("The project entered was not found")
        );

        project.setProjectDeleted(true);

        return projectRepositoryPort.deleteProject(project);
    }

}
