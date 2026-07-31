package com.keax.project.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.project.application.validation.ProjectStructureValidator;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.project.domain.ports.in.CreateProjectUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;
import com.keax.shared.domain.text.TextNormalizer;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateProjectUseCaseImpl implements CreateProjectUseCase {
    private final ProjectRepositoryPort projectRepositoryPort;
    private final ProjectStructureValidator projectStructureValidator;

    @Override
    public Project createProject(Project project) {

        projectStructureValidator.validate(project);

        project.setProjectTitle(TextNormalizer.uppercase(project.getProjectTitle()));
        projectRepositoryPort.findByTitle(
                project.getProjectTitle()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a project with this title");
                }
        );

        projectRepositoryPort.findByPosition(
                project.getProjectPosition()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("The project position is already filled");
                }
        );

        project.setProjectTitleEs(TextNormalizer.uppercase(project.getProjectTitleEs()));
        project.setProjectDescription(TextNormalizer.trimToNull(project.getProjectDescription()));
        project.setProjectDescriptionEs(TextNormalizer.trimToNull(project.getProjectDescriptionEs()));

        project.setProjectId(null);
        project.setProjectImages(new java.util.ArrayList<>());
        project.setProjectPublished(false);
        project.getProjectTechnologies().forEach(technology -> technology.setProjectTechnologyId(null));
        project.getProjectLinks().forEach(link -> link.setProjectLinkId(null));

        return projectRepositoryPort.createProject(project);
    }

}
