package com.keax.infrastructure.repositories;

import com.keax.infrastructure.entities.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaEducationRepository extends JpaRepository<EducationEntity, Long> {
    Optional<EducationEntity> findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(String educationTitle, Boolean deleted, Long institutionId);
    Optional<EducationEntity> findByEducationIdAndEducationDeleted(Long education_id, Boolean deleted);
    List<EducationEntity> findByEducationDeleted(Boolean deleted);
    Boolean existsByEducationIdAndEducationDeleted(Long education_id, Boolean deleted);
}
