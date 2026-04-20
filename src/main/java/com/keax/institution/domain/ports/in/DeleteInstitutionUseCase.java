package com.keax.institution.domain.ports.in;

import com.keax.institution.domain.model.Institution;

public interface DeleteInstitutionUseCase {

    Institution deleteInstitution(Long institutionId);

}
