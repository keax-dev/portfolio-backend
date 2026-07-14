package com.keax.skill.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.skill.domain.ports.in.DeleteSkillUseCase;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.skill.domain.model.Skill;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteSkillUseCaseImpl implements DeleteSkillUseCase {
    private final SkillRepositoryPort skillRepositoryPort;

    @Override
    public Skill deleteSkill(Long skillId) {

        Skill skill =  skillRepositoryPort.findBySkillIdAndSkillDeleted(
                skillId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The skill entered was not found")
        );

        skill.setSkillDeleted(true);

        return skillRepositoryPort.deleteSkill(skill);
    }

}
