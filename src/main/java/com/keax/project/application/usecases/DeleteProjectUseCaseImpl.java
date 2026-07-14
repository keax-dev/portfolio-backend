package com.keax.project.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.project.domain.ports.in.DeleteProjectUseCase;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteProjectUseCaseImpl implements DeleteProjectUseCase {
    private final ProjectRepositoryPort projectRepositoryPort;

    @Override
    public Project deleteProject(Long projectId) {

        Project project = projectRepositoryPort.findByProjectIdAndProjectDeleted(
                projectId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The project entered was not found")
        );

        project.setProjectDeleted(true);

        return projectRepositoryPort.deleteProject(project);
    }

}
