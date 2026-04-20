package com.keax.education.infrastructure.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.keax.education.infrastructure.out.persistence.entity.EducationEntity;
import java.util.Optional;
import java.util.List;

public interface JpaEducationRepository extends JpaRepository<EducationEntity, Long> {

    List<EducationEntity> findByEducationDeleted(Boolean deleted);
    Optional<EducationEntity> findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(String educationTitle, Boolean deleted, Long institutionId);
    Optional<EducationEntity> findByEducationIdAndEducationDeleted(Long education_id, Boolean deleted);
    Optional<EducationEntity> findByEducationPositionAndEducationDeleted(int position, Boolean deleted);
    Boolean existsByInstitution_InstitutionIdAndEducationDeleted(Long institutionId, Boolean deleted);

}
