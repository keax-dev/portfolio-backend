package com.keax.education.infrastructure.in.web.mapper;

import com.keax.education.infrastructure.in.web.dto.EducationDTO;
import com.keax.education.domain.model.Education;

public final class EducationWebMapper {

    public static Education toDomain(EducationDTO dto) {
        return new Education(
                dto.getEducationId(),
                dto.getEducationTitle(),
                dto.getEducationTitleEs(),
                dto.getEducationPlace(),
                dto.getEducationStart(),
                dto.getEducationStartEs(),
                dto.getEducationEnd(),
                dto.getEducationEndEs(),
                dto.getEducationPosition(),
                dto.getEducationDeleted(),
                dto.getInstitutionId(),
                dto.getInstitutionName(),
                dto.getInstitutionNameEs(),
                dto.getInstitutionUrl()
        );
    }

    public static EducationDTO fromDomain(Education education) {
        return new EducationDTO(
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
                education.getInstitutionId(),
                education.getInstitutionName(),
                education.getInstitutionNameEs(),
                education.getInstitutionUrl()
        );
    }

}
