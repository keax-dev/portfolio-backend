package com.keax.skill.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.skill.domain.ports.in.CreateSkillUseCase;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.skill.domain.model.Skill;

@Service
@Transactional
public class CreateSkillUseCaseImpl implements CreateSkillUseCase {

    @Autowired
    private SkillRepositoryPort skillRepositoryPort;

    @Override
    public Skill createSkill(Skill skill) {

        skill.setSkillName(skill.getSkillName().toUpperCase());

        skillRepositoryPort.findBySkillNameAndSkillDeleted(
                skill.getSkillName(),
                false
        ).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already a skill with this name");
                }
        );

        skillRepositoryPort.findBySkillPositionAndSkillDeleted(
                skill.getSkillPosition(),
                false
        ).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already a skill with this position");
                }
        );

        skill.setSkillPicture(null);
        skill.setSkillId(null);
        skill.setSkillDeleted(false);

        return skillRepositoryPort.createSkill(skill);
    }

}
