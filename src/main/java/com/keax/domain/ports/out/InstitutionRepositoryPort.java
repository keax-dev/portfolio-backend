package com.keax.domain.ports.out;

import com.keax.domain.models.Institution;
import com.keax.infrastructure.entities.InstitutionEntity;

import java.util.List;
import java.util.Optional;

public interface InstitutionRepositoryPort {
    Institution save(Institution institution);
    Institution updateInstitution(Long institution_id, Institution institution);
    List<Institution> getListInstitution();
    Boolean deleteInstitution(Long institution_id);
    Boolean existsByInstitutionName(String institution_name);
    Boolean existsByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    Boolean existsById(Long institution_id);
    Boolean existsByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted);
    List<Institution> findByInstitutionDeleted(Boolean deleted);
    Optional<Institution> findById(Long institution_id);
    Optional<Institution> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
}
