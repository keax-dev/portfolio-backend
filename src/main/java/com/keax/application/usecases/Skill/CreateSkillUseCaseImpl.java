package com.keax.application.usecases.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Skill.CreateSkillUseCase;
import com.keax.domain.ports.out.SkillRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Skill;

@Component
public class CreateSkillUseCaseImpl implements CreateSkillUseCase {

    @Autowired
    private SkillRepositoryPort skillRepositoryPort;

    @Override
    public Skill createSkill(Skill skill) {

        skill.setSkillName(skill.getSkillName().toUpperCase());

        skillRepositoryPort.findBySkillNameAndSkillDeleted(skill.getSkillName(), false).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already a skill with this name");
                }
        );

        skill.setSkillPicture(null);

        return skillRepositoryPort.createSkill(skill);
    }

}
