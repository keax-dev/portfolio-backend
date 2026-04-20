package com.keax.institution.domain.ports.in;

import com.keax.institution.domain.model.Institution;

public interface UpdateInstitutionUseCase {

    Institution updateInstitution(Long institutionId, Institution institution);

}
