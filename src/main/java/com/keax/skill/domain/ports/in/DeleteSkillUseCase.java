package com.keax.skill.domain.ports.in;

import com.keax.skill.domain.model.Skill;

public interface DeleteSkillUseCase {

    Skill deleteSkill(Long skillId);

}
