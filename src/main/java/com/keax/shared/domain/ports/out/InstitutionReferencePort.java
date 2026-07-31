package com.keax.shared.domain.ports.out;

public interface InstitutionReferencePort {

    boolean existsActiveInstitution(Long institutionId);

    boolean existsActiveEducationForInstitution(Long institutionId);

    boolean existsActiveCourseForInstitution(Long institutionId);
}
