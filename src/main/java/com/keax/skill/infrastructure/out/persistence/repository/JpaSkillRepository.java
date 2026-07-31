package com.keax.skill.infrastructure.out.persistence.repository;

import com.keax.skill.infrastructure.out.persistence.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface JpaSkillRepository extends JpaRepository<SkillEntity, Long> {

    List<SkillEntity> findAllByOrderBySkillPositionAsc();
    Optional<SkillEntity> findBySkillName(String skillName);
    Optional<SkillEntity> findBySkillPosition(int position);

}
