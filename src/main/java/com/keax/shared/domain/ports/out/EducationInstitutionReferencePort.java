package com.keax.shared.domain.ports.out;

public interface EducationInstitutionReferencePort {

    boolean existsActiveInstitution(Long institutionId);

    boolean existsActiveEducationForInstitution(Long institutionId);
}
