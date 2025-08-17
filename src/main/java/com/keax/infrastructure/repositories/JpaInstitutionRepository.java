package com.keax.infrastructure.repositories;

import com.keax.infrastructure.entities.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaInstitutionRepository extends JpaRepository<InstitutionEntity, Long> {
    Boolean existsByInstitutionName(String institutionName);
    Boolean existsByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    Boolean existsByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted);
    Optional<InstitutionEntity> findByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted);
    Optional<InstitutionEntity> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    List<InstitutionEntity> findByInstitutionDeleted(Boolean deleted);
}
