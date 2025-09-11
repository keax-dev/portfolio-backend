package com.keax.domain.ports.in.Skill;

import com.keax.domain.models.Skill;

public interface DeleteSkillUseCase {

    Skill deleteSkill(Long skillId);

}
