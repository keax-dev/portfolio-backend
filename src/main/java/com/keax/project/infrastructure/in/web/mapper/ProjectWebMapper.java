package com.keax.project.infrastructure.in.web.mapper;

import com.keax.project.infrastructure.in.web.dto.ProjectDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectImageDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectLinkDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectTechnologyDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectTechnologyRequestDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectLinkRequestDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectWriteDTO;
import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectImage;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.project.domain.model.Project;

public final class ProjectWebMapper {

    public static Project toDomain(ProjectWriteDTO dto) {
        return new Project(
                null,
                dto.getProjectTitle(),
                dto.getProjectTitleEs(),
                dto.getProjectDescription(),
                dto.getProjectDescriptionEs(),
                dto.getProjectPosition(),
                false,
                false,
                dto.getTechnologies().stream().map(ProjectWebMapper::technologyToDomain).toList(),
                dto.getLinks().stream().map(ProjectWebMapper::linkToDomain).toList(),
                new java.util.ArrayList<>()
        );
    }

    public static ProjectDTO fromDomain(Project project) {
        return new ProjectDTO(
                project.getProjectId(),
                project.getProjectTitle(),
                project.getProjectTitleEs(),
                project.getProjectDescription(),
                project.getProjectDescriptionEs(),
                project.getProjectPosition(),
                project.getProjectDeleted(),
                project.getProjectPublished(),
                project.getProjectTechnologies().stream().map(ProjectWebMapper::technologyFromDomain).toList(),
                project.getProjectLinks().stream().map(ProjectWebMapper::linkFromDomain).toList(),
                project.getProjectImages().stream().map(ProjectWebMapper::imageFromDomain).toList()
        );
    }

    private static ProjectTechnology technologyToDomain(ProjectTechnologyRequestDTO dto) {
        return new ProjectTechnology(
                null,
                dto.getTechnologyId(),
                null,
                dto.getPosition()
        );
    }

    private static ProjectTechnologyDTO technologyFromDomain(ProjectTechnology technology) {
        return new ProjectTechnologyDTO(
                technology.getProjectTechnologyId(),
                technology.getTechnologyId(),
                technology.getTechnologyName(),
                technology.getPosition()
        );
    }

    private static ProjectLink linkToDomain(ProjectLinkRequestDTO dto) {
        return new ProjectLink(null, dto.getType(), dto.getUrl(), dto.getPosition());
    }

    private static ProjectLinkDTO linkFromDomain(ProjectLink link) {
        return new ProjectLinkDTO(link.getProjectLinkId(), link.getType(), link.getUrl(), link.getPosition());
    }

    private static ProjectImageDTO imageFromDomain(ProjectImage image) {
        return new ProjectImageDTO(image.getProjectImageId(), image.getUrl(), image.getPosition());
    }

}
