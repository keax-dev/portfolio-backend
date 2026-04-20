package com.keax.technology.infrastructure.out.persistence.mapper;

import com.keax.project.infrastructure.out.persistence.mapper.ProjectPersistenceMapper;
import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.domain.model.Technology;
import java.util.ArrayList;

public final class TechnologyPersistenceMapper {

    public static Technology toDomain(TechnologyEntity entity) {
        return new Technology(
                entity.getTechnologyId(),
                entity.getTechnologyName(),
                entity.getTechnologyPosition(),
                entity.getTechnologyDeleted(),
                new ArrayList<>()
        );
    }

    public static Technology toDomainWithProjects(TechnologyEntity entity) {
        return new Technology(
                entity.getTechnologyId(),
                entity.getTechnologyName(),
                entity.getTechnologyPosition(),
                entity.getTechnologyDeleted(),
                entity.getProjectEntityList().stream().map(ProjectPersistenceMapper::toDomain).toList()
        );
    }

    public static TechnologyEntity toEntity(Technology technology) {
        return new TechnologyEntity(
                technology.getTechnologyId(),
                technology.getTechnologyName(),
                technology.getTechnologyPosition(),
                technology.getTechnologyDeleted(),
                new ArrayList<>()
        );
    }

    public static TechnologyEntity toReference(Long technologyId) {
        TechnologyEntity entity = new TechnologyEntity();
        entity.setTechnologyId(technologyId);
        return entity;
    }

}
