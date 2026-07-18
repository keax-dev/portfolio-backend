package com.keax.project.infrastructure.in.web.mapper;

import com.keax.project.infrastructure.in.web.dto.ProjectDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectLinkDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectTechnologyDTO;
import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.project.domain.model.Project;

public final class ProjectWebMapper {

    public static Project toDomain(ProjectDTO dto) {
        return new Project(
                dto.getProjectId(),
                dto.getProjectTitle(),
                dto.getProjectTitleEs(),
                dto.getProjectDescription(),
                dto.getProjectDescriptionEs(),
                dto.getProjectPicture(),
                dto.getProjectPosition(),
                dto.getProjectDeleted(),
                dto.getTechnologies().stream().map(ProjectWebMapper::technologyToDomain).toList(),
                dto.getLinks().stream().map(ProjectWebMapper::linkToDomain).toList()
        );
    }

    public static ProjectDTO fromDomain(Project project) {
        return new ProjectDTO(
                project.getProjectId(),
                project.getProjectTitle(),
                project.getProjectTitleEs(),
                project.getProjectDescription(),
                project.getProjectDescriptionEs(),
                project.getProjectPicture(),
                project.getProjectPosition(),
                project.getProjectDeleted(),
                project.getProjectTechnologies().stream().map(ProjectWebMapper::technologyFromDomain).toList(),
                project.getProjectLinks().stream().map(ProjectWebMapper::linkFromDomain).toList()
        );
    }

    private static ProjectTechnology technologyToDomain(ProjectTechnologyDTO dto) {
        return new ProjectTechnology(
                dto.getProjectTechnologyId(),
                dto.getTechnologyId(),
                dto.getTechnologyName(),
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

    private static ProjectLink linkToDomain(ProjectLinkDTO dto) {
        return new ProjectLink(dto.getProjectLinkId(), dto.getType(), dto.getUrl(), dto.getPosition());
    }

    private static ProjectLinkDTO linkFromDomain(ProjectLink link) {
        return new ProjectLinkDTO(link.getProjectLinkId(), link.getType(), link.getUrl(), link.getPosition());
    }

}
