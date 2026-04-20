package com.keax.institution.domain.ports.in;

import com.keax.institution.domain.model.Institution;

public interface CreateInstitutionUseCase {

    Institution createInstitution(Institution institution);

}
