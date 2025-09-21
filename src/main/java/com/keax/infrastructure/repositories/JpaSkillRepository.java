package com.keax.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.keax.infrastructure.entities.SkillEntity;
import java.util.Optional;
import java.util.List;

public interface JpaSkillRepository extends JpaRepository<SkillEntity, Long> {

    List<SkillEntity> findBySkillDeleted(Boolean skillDeleted);
    Optional<SkillEntity> findBySkillNameAndSkillDeleted(String skillName, Boolean deleted);
    Optional<SkillEntity> findBySkillIdAndSkillDeleted(Long skillId, Boolean deleted);
    Optional<SkillEntity> findBySkillPositionAndSkillDeleted(int position, Boolean deleted);

}
