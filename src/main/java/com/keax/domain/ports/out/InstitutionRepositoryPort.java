package com.keax.domain.ports.out;

import com.keax.domain.models.Institution;
import java.util.Optional;
import java.util.List;

public interface InstitutionRepositoryPort {

    Institution saveInstitution(Institution institution);
    Institution updateInstitution(Long institutionId, Institution institution);
    List<Institution> getListInstitution();
    List<Institution> findByInstitutionDeleted(Boolean deleted);
    Boolean deleteInstitution(Long institutionId);
    Optional<Institution> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    Optional<Institution> findByInstitutionIdAndInstitutionDeleted(Long institutionId, Boolean deleted);

}
