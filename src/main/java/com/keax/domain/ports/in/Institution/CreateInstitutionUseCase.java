package com.keax.domain.ports.in.Institution;

import com.keax.domain.models.Institution;

public interface CreateInstitutionUseCase {
    Institution createInstitution(Institution institution);
}
