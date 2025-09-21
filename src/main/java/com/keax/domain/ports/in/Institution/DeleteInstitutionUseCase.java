package com.keax.domain.ports.in.Institution;

import com.keax.domain.models.Institution;

public interface DeleteInstitutionUseCase {

    Institution deleteInstitution(Long institutionId);

}
