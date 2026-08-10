package com.keax.skill.domain.ports.out;

import com.keax.skill.domain.model.Skill;
import java.util.Optional;
import java.util.List;

public interface SkillRepositoryPort {

    Skill createSkill(Skill skill);
    Skill updateSkill(Skill skill);
    Skill deleteSkill(Skill skill);
    List<Skill> findAll();
    List<Skill> findVisible();
    Optional<Skill> findByName(String skillName);
    Optional<Skill> findById(Long skillId);
    Optional<Skill> findByPosition(int position);


}
