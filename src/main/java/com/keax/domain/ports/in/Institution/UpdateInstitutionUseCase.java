package com.keax.domain.ports.in.Institution;

import com.keax.domain.models.Institution;

public interface UpdateInstitutionUseCase {

    Institution updateInstitution(Long institution_id, Institution institution);

}
