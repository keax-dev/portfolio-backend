package com.keax.institution.infrastructure.out.persistence.mapper;

import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import com.keax.institution.domain.model.Institution;

public final class InstitutionPersistenceMapper {

    public static Institution toDomain(InstitutionEntity entity) {
        return new Institution(
                entity.getInstitutionId(),
                entity.getInstitutionName(),
                entity.getInstitutionNameEs(),
                entity.getInstitutionUrl(),
                entity.getInstitutionDeleted()
        );
    }

    public static InstitutionEntity toEntity(Institution institution) {
        return new InstitutionEntity(
                institution.getInstitutionId(),
                institution.getInstitutionName(),
                institution.getInstitutionNameEs(),
                institution.getInstitutionUrl(),
                institution.getInstitutionDeleted()
        );
    }

    public static InstitutionEntity toReference(Long institutionId) {
        InstitutionEntity entity = new InstitutionEntity();
        entity.setInstitutionId(institutionId);
        return entity;
    }

}
