package com.keax.domain.ports.out;

import com.keax.domain.models.Education;
import com.keax.infrastructure.entities.EducationEntity;

import java.util.List;
import java.util.Optional;

public interface EducationRepositoryPort {
    Education createEducation(Education education);
    Education updateEducation(Education education);
    List<Education> getListEducation();
    Education deleteEducation(Education education);
    Optional<Education> findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(String educationTitle, Boolean deleted, Long institutionId);
    List<Education> findByEducationDeleted(Boolean deleted);
    Boolean existsByEducationIdAndEducationDeleted(Long education_id, Boolean deleted);
    Optional<Education> findByEducationIdAndEducationDeleted(Long education_id, Boolean deleted);
}
