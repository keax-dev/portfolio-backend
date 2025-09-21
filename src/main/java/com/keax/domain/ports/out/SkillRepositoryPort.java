package com.keax.domain.ports.out;

import com.keax.domain.models.Skill;
import java.util.Optional;
import java.util.List;

public interface SkillRepositoryPort {

    Skill createSkill(Skill skill);
    Skill updateSkill(Skill skill);
    Skill deleteSkill(Skill skill);
    List<Skill> findBySkillDeleted(Boolean deleted);
    List<Skill> getListSkill();
    Optional<Skill> findBySkillNameAndSkillDeleted(String skillName, Boolean deleted);
    Optional<Skill> findBySkillIdAndSkillDeleted(Long skillId, Boolean deleted);
    Optional<Skill> findBySkillPositionAndSkillDeleted(int position, Boolean deleted);


}
