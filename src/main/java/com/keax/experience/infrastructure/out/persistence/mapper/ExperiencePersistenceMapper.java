package com.keax.experience.infrastructure.out.persistence.mapper;

import com.keax.experience.domain.model.Experience;
import com.keax.experience.infrastructure.out.persistence.entity.ExperienceEntity;

public final class ExperiencePersistenceMapper {

    private ExperiencePersistenceMapper() {
    }

    public static Experience toDomain(ExperienceEntity entity) {
        return new Experience(
                entity.getExperienceId(),
                entity.getExperienceRole(),
                entity.getExperienceRoleEs(),
                entity.getExperienceCompany(),
                entity.getExperienceDescription(),
                entity.getExperienceDescriptionEs(),
                entity.getExperienceStart(),
                entity.getExperienceStartEs(),
                entity.getExperienceEnd(),
                entity.getExperienceEndEs(),
                entity.getExperiencePosition(),
                entity.getExperienceVisible(),
                entity.getVersion()
        );
    }

    public static ExperienceEntity toEntity(Experience experience) {
        return new ExperienceEntity(
                experience.getExperienceId(),
                experience.getExperienceRole(),
                experience.getExperienceRoleEs(),
                experience.getExperienceCompany(),
                experience.getExperienceDescription(),
                experience.getExperienceDescriptionEs(),
                experience.getExperienceStart(),
                experience.getExperienceStartEs(),
                experience.getExperienceEnd(),
                experience.getExperienceEndEs(),
                experience.getExperiencePosition(),
                experience.getExperienceVisible(),
                false,
                experience.getVersion()
        );
    }
}
