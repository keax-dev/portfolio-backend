package com.keax.application.usecases.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Skill.DeleteSkillUseCase;
import com.keax.domain.ports.out.SkillRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Skill;

@Component
public class DeleteSkillUseCaseImpl implements DeleteSkillUseCase {

    @Autowired
    private SkillRepositoryPort skillRepositoryPort;

    @Override
    public Skill deleteSkill(Long skillId) {

        Skill skill =  skillRepositoryPort.findBySkillIdAndSkillDeleted(skillId, false).orElseThrow(
                () -> new ExceptionAlert("The skill entered was not found")
        );

        skill.setSkillDeleted(true);

        return skillRepositoryPort.deleteSkill(skill);
    }

}
