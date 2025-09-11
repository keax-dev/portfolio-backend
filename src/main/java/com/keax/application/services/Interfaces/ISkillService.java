package com.keax.application.services.Interfaces;

import com.keax.domain.models.Skill;
import java.util.List;

public interface ISkillService {

    Skill createSkill(Skill skill);
    Skill updateSkill(Long skillId, Skill skill);
    Skill deleteSkill(Long skillId);
    List<Skill> findBySkillDeleted(Boolean deleted);
    List<Skill> getListSkill();

}
