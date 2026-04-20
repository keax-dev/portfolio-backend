package com.keax.skill.domain.ports.in;

import com.keax.skill.domain.model.Skill;

public interface UpdateSkillUseCase {

    Skill updateSkill(Long skillId, Skill skill);

}
