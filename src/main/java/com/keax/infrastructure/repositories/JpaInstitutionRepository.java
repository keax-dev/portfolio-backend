package com.keax.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.keax.infrastructure.entities.InstitutionEntity;
import java.util.Optional;
import java.util.List;

public interface JpaInstitutionRepository extends JpaRepository<InstitutionEntity, Long> {

    Boolean existsByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    Boolean existsByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted);
    List<InstitutionEntity> findByInstitutionDeleted(Boolean deleted);
    Optional<InstitutionEntity> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    Optional<InstitutionEntity> findByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted);

}
