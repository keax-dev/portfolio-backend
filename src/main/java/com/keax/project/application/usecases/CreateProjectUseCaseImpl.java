package com.keax.project.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.project.application.validation.ProjectStructureValidator;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.project.domain.ports.in.CreateProjectUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateProjectUseCaseImpl implements CreateProjectUseCase {
    private final ProjectRepositoryPort projectRepositoryPort;
    private final ProjectStructureValidator projectStructureValidator;

    @Override
    public Project createProject(Project project) {

        projectStructureValidator.validate(project);

        project.setProjectTitle(project.getProjectTitle().toUpperCase());
        projectRepositoryPort.findByProjectTitleAndProjectDeleted(
                project.getProjectTitle(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a project with this title");
                }
        );

        projectRepositoryPort.findByProjectPositionAndProjectDeleted(
                project.getProjectPosition(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("The project position is already filled");
                }
        );

        project.setProjectTitleEs(project.getProjectTitleEs().toUpperCase());

        project.setProjectId(null);
        project.setProjectImages(new java.util.ArrayList<>());
        project.setProjectDeleted(false);
        project.getProjectTechnologies().forEach(technology -> technology.setProjectTechnologyId(null));
        project.getProjectLinks().forEach(link -> link.setProjectLinkId(null));

        return projectRepositoryPort.createProject(project);
    }

}
