package com.keax.institution.infrastructure.out.persistence.repository;

import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface JpaInstitutionRepository extends JpaRepository<InstitutionEntity, Long> {

    List<InstitutionEntity> findByInstitutionDeleted(Boolean deleted);
    Optional<InstitutionEntity> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted);
    Optional<InstitutionEntity> findByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted);

}
