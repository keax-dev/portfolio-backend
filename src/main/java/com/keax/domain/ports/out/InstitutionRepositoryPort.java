package com.keax.domain.ports.out;

import com.keax.domain.models.Institution;

import java.util.List;
import java.util.Optional;

public interface InstitutionRepositoryPort {
    Institution save(Institution institution);
    Optional<Institution> updateInstitution(Long institution_id, Institution institution);
    List<Institution> getListInstitution();
    Boolean deleteInstitution(Long institution_id);
    Boolean existsByInstitutionNameIgnoreCase(String institution_name);
}
