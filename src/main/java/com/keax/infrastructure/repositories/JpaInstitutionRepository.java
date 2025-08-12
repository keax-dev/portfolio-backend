package com.keax.infrastructure.repositories;

import com.keax.infrastructure.entities.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInstitutionRepository extends JpaRepository<InstitutionEntity, Long> {
    boolean existsByInstitutionNameIgnoreCase(String institutionName);
}
