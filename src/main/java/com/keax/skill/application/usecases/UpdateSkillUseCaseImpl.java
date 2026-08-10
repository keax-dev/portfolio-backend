package com.keax.skill.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.skill.domain.ports.in.UpdateSkillUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.skill.domain.model.Skill;
import com.keax.shared.domain.text.TextNormalizer;
import com.keax.skill.domain.model.SkillCategory;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateSkillUseCaseImpl implements UpdateSkillUseCase {
    private final SkillRepositoryPort skillRepositoryPort;

    @Override
    public Skill updateSkill(Long skillId, Skill skill) {

        Skill skillUpdate = skillRepositoryPort.findById(skillId).orElseThrow(
                () -> new ResourceNotFoundException("The skill entered was not found")
        );

        skillUpdate.setSkillName(TextNormalizer.uppercase(skill.getSkillName()));
        skillRepositoryPort.findByName(
                skillUpdate.getSkillName()
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getSkillId(), skillUpdate.getSkillId())){
                        throw new ResourceConflictException("The name of the skill to be updated is already registered");
                    }
                }
        );

        skillUpdate.setSkillPosition(skill.getSkillPosition());
        skillRepositoryPort.findByPosition(
                skillUpdate.getSkillPosition()
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getSkillId(), skillUpdate.getSkillId())){
                        throw new ResourceConflictException("The position of the skill to be updated is already registered");
                    }
                }
        );

        skillUpdate.setSkillCategory(Objects.requireNonNullElse(
                skill.getSkillCategory(),
                SkillCategory.OTHER
        ));
        skillUpdate.setSkillVisible(Objects.requireNonNullElse(skill.getSkillVisible(), true));

        return skillRepositoryPort.updateSkill(skillUpdate);
    }

}
