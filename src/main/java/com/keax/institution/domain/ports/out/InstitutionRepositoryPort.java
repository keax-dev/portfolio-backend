package com.keax.institution.domain.ports.out;

import com.keax.institution.domain.model.Institution;
import java.util.Optional;
import java.util.List;

public interface InstitutionRepositoryPort {

    Institution saveInstitution(Institution institution);
    Institution updateInstitution(Institution institution);
    Institution deleteInstitution(Institution institution);
    List<Institution> findAll();
    Optional<Institution> findByName(String institutionName);
    Optional<Institution> findById(Long institutionId);

}
