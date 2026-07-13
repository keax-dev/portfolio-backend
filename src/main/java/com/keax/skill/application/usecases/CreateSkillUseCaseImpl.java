package com.keax.skill.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.skill.domain.ports.in.CreateSkillUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.skill.domain.model.Skill;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateSkillUseCaseImpl implements CreateSkillUseCase {
    private final SkillRepositoryPort skillRepositoryPort;

    @Override
    public Skill createSkill(Skill skill) {

        skill.setSkillName(skill.getSkillName().toUpperCase());

        skillRepositoryPort.findBySkillNameAndSkillDeleted(
                skill.getSkillName(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a skill with this name");
                }
        );

        skillRepositoryPort.findBySkillPositionAndSkillDeleted(
                skill.getSkillPosition(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a skill with this position");
                }
        );

        skill.setSkillPicture(null);
        skill.setSkillId(null);
        skill.setSkillDeleted(false);

        return skillRepositoryPort.createSkill(skill);
    }

}
