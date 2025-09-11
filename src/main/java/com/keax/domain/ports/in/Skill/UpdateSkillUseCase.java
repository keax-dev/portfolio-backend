package com.keax.domain.ports.in.Skill;

import com.keax.domain.models.Skill;

public interface UpdateSkillUseCase {

    Skill updateSkill(Long skillId, Skill skill);

}
