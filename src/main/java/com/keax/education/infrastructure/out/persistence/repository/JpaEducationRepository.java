package com.keax.education.infrastructure.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import com.keax.education.infrastructure.out.persistence.entity.EducationEntity;
import java.util.Optional;
import java.util.List;

public interface JpaEducationRepository extends JpaRepository<EducationEntity, Long> {

    @EntityGraph(attributePaths = "institution")
    List<EducationEntity> findAllByOrderByEducationPositionAsc();

    @EntityGraph(attributePaths = "institution")
    List<EducationEntity> findAllByEducationVisibleTrueOrderByEducationPositionAsc();

    @EntityGraph(attributePaths = "institution")
    Optional<EducationEntity> findByEducationTitleAndInstitution_InstitutionId(
            String educationTitle,
            Long institutionId
    );

    @Override
    @EntityGraph(attributePaths = "institution")
    Optional<EducationEntity> findById(Long educationId);

    @EntityGraph(attributePaths = "institution")
    Optional<EducationEntity> findByEducationPosition(int position);

}
