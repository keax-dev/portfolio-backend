package com.keax.education.domain.ports.out;

import com.keax.education.domain.model.Education;
import java.util.Optional;
import java.util.List;

public interface EducationRepositoryPort {

    Education createEducation(Education education);
    Education updateEducation(Education education);
    Education deleteEducation(Education education);
    List<Education> findByEducationDeleted(Boolean deleted);
    List<Education> getListEducation();
    Optional<Education> findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(String educationTitle, Boolean deleted, Long institutionId);
    Optional<Education> findByEducationIdAndEducationDeleted(Long educationId, Boolean deleted);
    Optional<Education> findByEducationPositionAndEducationDeleted(int position, Boolean deleted);
    Boolean existsByInstitution_InstitutionIdAndEducationDeleted(Long institutionId, Boolean deleted);

}
