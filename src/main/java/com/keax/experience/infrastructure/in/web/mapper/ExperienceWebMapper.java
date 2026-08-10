package com.keax.experience.infrastructure.in.web.mapper;

import com.keax.experience.domain.model.Experience;
import com.keax.experience.infrastructure.in.web.dto.ExperienceDTO;

public final class ExperienceWebMapper {

    private ExperienceWebMapper() {
    }

    public static Experience toDomain(ExperienceDTO dto) {
        return new Experience(
                dto.getExperienceId(),
                dto.getExperienceRole(),
                dto.getExperienceRoleEs(),
                dto.getExperienceCompany(),
                dto.getExperienceDescription(),
                dto.getExperienceDescriptionEs(),
                dto.getExperienceStart(),
                dto.getExperienceStartEs(),
                dto.getExperienceEnd(),
                dto.getExperienceEndEs(),
                dto.getExperiencePosition(),
                dto.getExperienceVisible(),
                null
        );
    }

    public static ExperienceDTO fromDomain(Experience experience) {
        return new ExperienceDTO(
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
                experience.getExperienceVisible()
        );
    }
}
