package com.keax.skill.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.skill.domain.ports.in.CreateSkillUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.skill.domain.model.Skill;
import com.keax.shared.domain.text.TextNormalizer;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateSkillUseCaseImpl implements CreateSkillUseCase {
    private final SkillRepositoryPort skillRepositoryPort;

    @Override
    public Skill createSkill(Skill skill) {

        skill.setSkillName(TextNormalizer.uppercase(skill.getSkillName()));

        skillRepositoryPort.findByName(
                skill.getSkillName()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a skill with this name");
                }
        );

        skillRepositoryPort.findByPosition(
                skill.getSkillPosition()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a skill with this position");
                }
        );

        skill.setSkillPicture(null);
        skill.setSkillId(null);
        return skillRepositoryPort.createSkill(skill);
    }

}
