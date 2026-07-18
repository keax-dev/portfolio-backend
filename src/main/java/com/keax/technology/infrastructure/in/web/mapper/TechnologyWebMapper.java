package com.keax.technology.infrastructure.in.web.mapper;

import com.keax.technology.infrastructure.in.web.dto.TechnologyDTO;
import com.keax.technology.domain.model.Technology;

public final class TechnologyWebMapper {

    public static Technology toDomain(TechnologyDTO dto) {
        return new Technology(
                dto.getTechnologyId(),
                dto.getTechnologyName(),
                dto.getTechnologyPosition(),
                dto.getTechnologyDeleted()
        );
    }

    public static TechnologyDTO fromDomain(Technology technology) {
        return new TechnologyDTO(
                technology.getTechnologyId(),
                technology.getTechnologyName(),
                technology.getTechnologyPosition(),
                technology.getTechnologyDeleted()
        );
    }

}
