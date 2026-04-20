package com.keax.skill.domain.ports.in;

import com.keax.skill.domain.model.Skill;

public interface CreateSkillUseCase {

    Skill createSkill(Skill skill);

}
