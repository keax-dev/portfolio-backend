package com.keax.project.infrastructure.out.persistence.mapper;

import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.keax.project.infrastructure.out.persistence.entity.ProjectLinkEntity;
import com.keax.project.infrastructure.out.persistence.entity.ProjectTechnologyEntity;
import com.keax.technology.infrastructure.out.persistence.mapper.TechnologyPersistenceMapper;
import com.keax.project.domain.model.Project;
import java.util.Comparator;

public final class ProjectPersistenceMapper {

    public static Project toDomain(ProjectEntity entity) {
        return new Project(
                entity.getProjectId(),
                entity.getProjectTitle(),
                entity.getProjectTitleEs(),
                entity.getProjectDescription(),
                entity.getProjectDescriptionEs(),
                entity.getProjectPicture(),
                entity.getProjectPosition(),
                entity.getProjectDeleted(),
                entity.getProjectTechnologies().stream()
                        .map(ProjectPersistenceMapper::technologyToDomain)
                        .sorted(Comparator.comparingInt(ProjectTechnology::getPosition))
                        .toList(),
                entity.getProjectLinks().stream()
                        .map(ProjectPersistenceMapper::linkToDomain)
                        .sorted(Comparator.comparingInt(ProjectLink::getPosition))
                        .toList()
        );
    }

    public static ProjectEntity toEntity(Project project) {
        ProjectEntity entity = new ProjectEntity();
        entity.setProjectId(project.getProjectId());
        entity.setProjectTitle(project.getProjectTitle());
        entity.setProjectTitleEs(project.getProjectTitleEs());
        entity.setProjectDescription(project.getProjectDescription());
        entity.setProjectDescriptionEs(project.getProjectDescriptionEs());
        entity.setProjectPicture(project.getProjectPicture());
        entity.setProjectPosition(project.getProjectPosition());
        entity.setProjectDeleted(project.getProjectDeleted());

        project.getProjectTechnologies().forEach(technology -> entity.getProjectTechnologies().add(
                new ProjectTechnologyEntity(
                        technology.getProjectTechnologyId(),
                        entity,
                        TechnologyPersistenceMapper.toReference(technology.getTechnologyId()),
                        technology.getPosition()
                )
        ));
        project.getProjectLinks().forEach(link -> entity.getProjectLinks().add(
                new ProjectLinkEntity(
                        link.getProjectLinkId(),
                        entity,
                        link.getType(),
                        link.getUrl(),
                        link.getPosition()
                )
        ));

        return entity;
    }

    private static ProjectTechnology technologyToDomain(ProjectTechnologyEntity entity) {
        return new ProjectTechnology(
                entity.getProjectTechnologyId(),
                entity.getTechnology().getTechnologyId(),
                entity.getTechnology().getTechnologyName(),
                entity.getPosition()
        );
    }

    private static ProjectLink linkToDomain(ProjectLinkEntity entity) {
        return new ProjectLink(
                entity.getProjectLinkId(),
                entity.getType(),
                entity.getUrl(),
                entity.getPosition()
        );
    }

}
