package com.keax.project.application.usecases;

import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.project.domain.ports.in.DeleteProjectUseCase;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;

@Service
@Transactional
public class DeleteProjectUseCaseImpl implements DeleteProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Override
    public Project deleteProject(Long projectId) {

        Project project = projectRepositoryPort.findByProjectIdAndProjectDeleted(
                projectId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The project entered was not found")
        );

        project.setProjectDeleted(true);

        return projectRepositoryPort.deleteProject(project);
    }

}
