package com.keax.education.infrastructure.out.persistence.mapper;

import com.keax.institution.infrastructure.out.persistence.mapper.InstitutionPersistenceMapper;
import com.keax.education.infrastructure.out.persistence.entity.EducationEntity;
import com.keax.education.domain.model.Education;

public final class EducationPersistenceMapper {

    public static Education toDomain(EducationEntity entity) {
        return new Education(
                entity.getEducationId(),
                entity.getEducationTitle(),
                entity.getEducationTitleEs(),
                entity.getEducationPlace(),
                entity.getEducationStart(),
                entity.getEducationStartEs(),
                entity.getEducationEnd(),
                entity.getEducationEndEs(),
                entity.getEducationPosition(),
                entity.getEducationDeleted(),
                entity.getInstitution().getInstitutionId(),
                entity.getInstitution().getInstitutionName(),
                entity.getInstitution().getInstitutionNameEs(),
                entity.getInstitution().getInstitutionUrl()
        );
    }

    public static EducationEntity toEntity(Education education) {
        return new EducationEntity(
                education.getEducationId(),
                education.getEducationTitle(),
                education.getEducationTitleEs(),
                education.getEducationPlace(),
                education.getEducationStart(),
                education.getEducationStartEs(),
                education.getEducationEnd(),
                education.getEducationEndEs(),
                education.getEducationPosition(),
                education.getEducationDeleted(),
                InstitutionPersistenceMapper.toReference(education.getInstitutionId())
        );
    }

}
