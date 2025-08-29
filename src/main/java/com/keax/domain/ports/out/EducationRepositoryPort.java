package com.keax.domain.ports.out;

import com.keax.domain.models.Education;
import java.util.Optional;
import java.util.List;

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
