package com.keax.project.infrastructure.in.web.mapper;

import com.keax.project.infrastructure.in.web.dto.ProjectDTO;
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
                dto.getProjectDeploy(),
                dto.getProjectGithub(),
                dto.getProjectPosition(),
                dto.getTechnologyId(),
                dto.getTechnologyName(),
                dto.getProjectDeleted()
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
                project.getProjectDeploy(),
                project.getProjectGithub(),
                project.getProjectPosition(),
                project.getTechnologyId(),
                project.getTechnologyName(),
                project.getProjectDeleted()
        );
    }

}
