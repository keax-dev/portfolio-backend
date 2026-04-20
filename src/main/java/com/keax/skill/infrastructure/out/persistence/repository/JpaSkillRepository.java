package com.keax.skill.infrastructure.out.persistence.repository;

import com.keax.skill.infrastructure.out.persistence.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface JpaSkillRepository extends JpaRepository<SkillEntity, Long> {

    List<SkillEntity> findBySkillDeleted(Boolean skillDeleted);
    Optional<SkillEntity> findBySkillNameAndSkillDeleted(String skillName, Boolean deleted);
    Optional<SkillEntity> findBySkillIdAndSkillDeleted(Long skillId, Boolean deleted);
    Optional<SkillEntity> findBySkillPositionAndSkillDeleted(int position, Boolean deleted);

}
