package com.keax.domain.ports.in.Institution;

import com.keax.domain.models.Institution;
import java.util.Optional;

public interface UpdateInstitutionUseCase {
    Optional<Institution> updateInstitution(Long institution_id, Institution institution);
}
