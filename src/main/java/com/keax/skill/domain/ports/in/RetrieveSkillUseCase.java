package com.keax.skill.domain.ports.in;

import com.keax.skill.domain.model.Skill;
import java.util.List;

public interface RetrieveSkillUseCase {

    List<Skill> findBySkillDeleted(Boolean deleted);
    List<Skill> getListSkill();

}
