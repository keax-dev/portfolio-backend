package com.keax.project.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.project.application.validation.ProjectStructureValidator;
import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.project.domain.ports.in.UpdateProjectUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;
import com.keax.shared.domain.text.TextNormalizer;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProjectUseCaseImpl implements UpdateProjectUseCase {
    private final ProjectRepositoryPort projectRepositoryPort;
    private final ProjectStructureValidator projectStructureValidator;

    @Override
    public Project updateProject(Long projectId, Project project) {

        Project projectUpdate = projectRepositoryPort.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("The project entered was not found")
        );

        projectStructureValidator.validate(project);

        projectUpdate.setProjectTitle(TextNormalizer.uppercase(project.getProjectTitle()));
        projectRepositoryPort.findByTitle(
                projectUpdate.getProjectTitle()
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getProjectId(), projectUpdate.getProjectId())){
                        throw new ResourceConflictException("The title of the project to be updated is already registered");
                    }
                }
        );

        projectUpdate.setProjectPosition(project.getProjectPosition());
        projectRepositoryPort.findByPosition(
                projectUpdate.getProjectPosition()
        ).ifPresent(
                e -> {
                    if (!Objects.equals(e.getProjectId(), projectUpdate.getProjectId())){
                        throw new ResourceConflictException("The project position is already filled");
                    }
                }
        );

        projectUpdate.setProjectTitleEs(TextNormalizer.uppercase(project.getProjectTitleEs()));
        projectUpdate.setProjectDescription(TextNormalizer.trimToNull(project.getProjectDescription()));
        projectUpdate.setProjectDescriptionEs(TextNormalizer.trimToNull(project.getProjectDescriptionEs()));
        preserveAssociationIds(projectUpdate, project);
        projectUpdate.setProjectTechnologies(project.getProjectTechnologies());
        projectUpdate.setProjectLinks(project.getProjectLinks());
        return projectRepositoryPort.updateProject(projectUpdate);
    }

    private void preserveAssociationIds(Project existing, Project requested) {
        requested.getProjectTechnologies().forEach(technology -> technology.setProjectTechnologyId(
                existing.getProjectTechnologies().stream()
                        .filter(current -> Objects.equals(current.getTechnologyId(), technology.getTechnologyId()))
                        .map(ProjectTechnology::getProjectTechnologyId)
                        .findFirst()
                        .orElse(null)
        ));

        requested.getProjectLinks().forEach(link -> link.setProjectLinkId(
                existing.getProjectLinks().stream()
                        .filter(current -> Objects.equals(current.getType(), link.getType()))
                        .map(ProjectLink::getProjectLinkId)
                        .findFirst()
                        .orElse(null)
        ));
    }

}
