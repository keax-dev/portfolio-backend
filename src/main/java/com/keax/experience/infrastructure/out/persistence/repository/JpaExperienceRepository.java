package com.keax.experience.infrastructure.out.persistence.repository;

import com.keax.experience.infrastructure.out.persistence.entity.ExperienceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaExperienceRepository extends JpaRepository<ExperienceEntity, Long> {

    List<ExperienceEntity> findAllByOrderByExperiencePositionAsc();

    List<ExperienceEntity> findAllByExperienceVisibleTrueOrderByExperiencePositionAsc();

    Optional<ExperienceEntity> findByExperiencePosition(int position);
}
