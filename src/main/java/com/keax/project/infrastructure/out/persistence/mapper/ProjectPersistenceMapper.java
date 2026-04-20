package com.keax.project.infrastructure.out.persistence.mapper;

import com.keax.technology.infrastructure.out.persistence.mapper.TechnologyPersistenceMapper;
import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.keax.project.domain.model.Project;

public final class ProjectPersistenceMapper {

    public static Project toDomain(ProjectEntity entity) {
        return new Project(
                entity.getProjectId(),
                entity.getProjectTitle(),
                entity.getProjectTitleEs(),
                entity.getProjectDescription(),
                entity.getProjectDescriptionEs(),
                entity.getProjectPicture(),
                entity.getProjectDeploy(),
                entity.getProjectGithub(),
                entity.getProjectPosition(),
                entity.getTechnology().getTechnologyId(),
                entity.getTechnology().getTechnologyName(),
                entity.getProjectDeleted()
        );
    }

    public static ProjectEntity toEntity(Project project) {
        return new ProjectEntity(
                project.getProjectId(),
                project.getProjectTitle(),
                project.getProjectTitleEs(),
                project.getProjectDescription(),
                project.getProjectDescriptionEs(),
                project.getProjectPicture(),
                project.getProjectDeploy(),
                project.getProjectGithub(),
                project.getProjectPosition(),
                project.getProjectDeleted(),
                TechnologyPersistenceMapper.toReference(project.getTechnologyId())
        );
    }

}
