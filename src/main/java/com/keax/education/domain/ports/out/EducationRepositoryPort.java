package com.keax.education.domain.ports.out;

import com.keax.education.domain.model.Education;
import java.util.Optional;
import java.util.List;

public interface EducationRepositoryPort {

    Education createEducation(Education education);
    Education updateEducation(Education education);
    Education deleteEducation(Education education);
    List<Education> findAll();
    List<Education> findVisible();
    Optional<Education> findByTitleAndInstitutionId(String educationTitle, Long institutionId);
    Optional<Education> findById(Long educationId);
    Optional<Education> findByPosition(int position);

}
