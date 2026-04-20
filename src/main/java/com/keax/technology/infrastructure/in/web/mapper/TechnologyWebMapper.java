package com.keax.technology.infrastructure.in.web.mapper;

import com.keax.project.infrastructure.in.web.mapper.ProjectWebMapper;
import com.keax.technology.infrastructure.in.web.dto.TechnologyDTO;
import com.keax.technology.domain.model.Technology;
import java.util.ArrayList;

public final class TechnologyWebMapper {

    public static Technology toDomain(TechnologyDTO dto) {
        return new Technology(
                dto.getTechnologyId(),
                dto.getTechnologyName(),
                dto.getTechnologyPosition(),
                dto.getTechnologyDeleted(),
                dto.getProjectList() == null
                        ? new ArrayList<>()
                        : dto.getProjectList().stream().map(ProjectWebMapper::toDomain).toList()
        );
    }

    public static TechnologyDTO fromDomain(Technology technology) {
        return new TechnologyDTO(
                technology.getTechnologyId(),
                technology.getTechnologyName(),
                technology.getTechnologyPosition(),
                technology.getTechnologyDeleted(),
                technology.getProjectList().stream().map(ProjectWebMapper::fromDomain).toList()
        );
    }

}
