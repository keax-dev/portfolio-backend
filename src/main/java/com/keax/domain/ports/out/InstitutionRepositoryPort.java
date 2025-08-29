package com.keax.domain.ports.out;

import com.keax.domain.models.Institution;
import java.util.Optional;
import java.util.List;

public interface InstitutionRepositoryPort {

    Institution saveInstitution(Institution institution);
    Institution updateInstitution(Long institution_id, Institution institution);
    List<Institution> getListInstitution();
    List<Institution> findByInstitutionDeleted(Boolean deleted);
    Boolean deleteInstitution(Long institution_id);
    Boolean existsByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    Boolean existsByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted);
    Optional<Institution> findById(Long institution_id);
    Optional<Institution> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    Optional<Institution> findByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted);

}
