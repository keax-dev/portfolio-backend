package com.keax.institution.infrastructure.in.web.mapper;

import com.keax.institution.infrastructure.in.web.dto.InstitutionDTO;
import com.keax.institution.domain.model.Institution;

public final class InstitutionWebMapper {

    public static Institution toDomain(InstitutionDTO dto) {
        return new Institution(
                dto.getInstitutionId(),
                dto.getInstitutionName(),
                dto.getInstitutionNameEs(),
                dto.getInstitutionUrl(),
                dto.getInstitutionDeleted()
        );
    }

    public static InstitutionDTO fromDomain(Institution institution) {
        return new InstitutionDTO(
                institution.getInstitutionId(),
                institution.getInstitutionName(),
                institution.getInstitutionNameEs(),
                institution.getInstitutionUrl(),
                institution.getInstitutionDeleted()
        );
    }

}
