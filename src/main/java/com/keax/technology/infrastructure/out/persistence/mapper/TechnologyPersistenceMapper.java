package com.keax.technology.infrastructure.out.persistence.mapper;

import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.domain.model.Technology;

public final class TechnologyPersistenceMapper {

    public static Technology toDomain(TechnologyEntity entity) {
        return new Technology(
                entity.getTechnologyId(),
                entity.getTechnologyName(),
                entity.getVersion()
        );
    }

    public static TechnologyEntity toEntity(Technology technology) {
        return new TechnologyEntity(
                technology.getTechnologyId(),
                technology.getTechnologyName(),
                false,
                technology.getVersion()
        );
    }

    public static TechnologyEntity toReference(Long technologyId) {
        TechnologyEntity entity = new TechnologyEntity();
        entity.setTechnologyId(technologyId);
        return entity;
    }

}
