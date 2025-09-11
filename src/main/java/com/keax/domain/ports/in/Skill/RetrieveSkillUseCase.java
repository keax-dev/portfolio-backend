package com.keax.domain.ports.in.Skill;

import com.keax.domain.models.Skill;
import java.util.List;

public interface RetrieveSkillUseCase {

    List<Skill> findBySkillDeleted(Boolean deleted);
    List<Skill> getListSkill();

}
