package com.keax.project.application.validation;

import com.keax.project.domain.model.Project;
import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectLinkType;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProjectStructureValidator {

    private final TechnologyRepositoryPort technologyRepositoryPort;

    public void validate(Project project) {
        List<ProjectTechnology> technologies = project.getProjectTechnologies();
        if (technologies == null || technologies.isEmpty()) {
            throw new ExceptionAlert("At least one project technology is required");
        }

        Set<Long> technologyIds = new HashSet<>();
        Set<Integer> technologyPositions = new HashSet<>();
        for (ProjectTechnology technology : technologies) {
            if (technology.getTechnologyId() == null) {
                throw new ExceptionAlert("The technology is required");
            }
            if (!technologyIds.add(technology.getTechnologyId())) {
                throw new ResourceConflictException("A technology cannot be repeated within the project");
            }
            if (technology.getPosition() < 1 || !technologyPositions.add(technology.getPosition())) {
                throw new ResourceConflictException("Project technology positions must be positive and unique");
            }

            technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(
                    technology.getTechnologyId(),
                    false
            ).orElseThrow(() -> new ResourceNotFoundException("The technology entered was not found"));
        }

        if (project.getProjectLinks() == null) {
            project.setProjectLinks(new ArrayList<>());
        }
        validateLinks(project.getProjectLinks());
    }

    private void validateLinks(List<ProjectLink> links) {
        Set<Integer> positions = new HashSet<>();
        Set<ProjectLinkType> types = new HashSet<>();
        for (ProjectLink link : links) {
            if (link.getType() == null || link.getUrl() == null || link.getUrl().isBlank()) {
                throw new ExceptionAlert("Project links require a type and url");
            }
            if (!types.add(link.getType())) {
                throw new ResourceConflictException("A project link type cannot be repeated");
            }
            if (link.getPosition() < 1 || !positions.add(link.getPosition())) {
                throw new ResourceConflictException("Project link positions must be positive and unique");
            }
            link.setUrl(link.getUrl().trim());
        }
    }
}
