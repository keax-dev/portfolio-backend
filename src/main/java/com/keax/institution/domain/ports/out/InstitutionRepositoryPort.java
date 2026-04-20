package com.keax.institution.domain.ports.out;

import com.keax.institution.domain.model.Institution;
import java.util.Optional;
import java.util.List;

public interface InstitutionRepositoryPort {

    Institution saveInstitution(Institution institution);
    Institution updateInstitution(Institution institution);
    Institution deleteInstitution(Institution institution);
    List<Institution> getListInstitution();
    List<Institution> findByInstitutionDeleted(Boolean deleted);
    Optional<Institution> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    Optional<Institution> findByInstitutionIdAndInstitutionDeleted(Long institutionId, Boolean deleted);

}
