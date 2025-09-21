package com.keax.application.usecases.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Skill.UpdateSkillUseCase;
import com.keax.domain.ports.out.SkillRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Skill;
import java.util.Objects;

@Component
public class UpdateSkillUseCaseImpl implements UpdateSkillUseCase {

    @Autowired
    private SkillRepositoryPort skillRepositoryPort;

    @Override
    public Skill updateSkill(Long skillId, Skill skill) {

        Skill skillUpdate = skillRepositoryPort.findBySkillIdAndSkillDeleted(skillId, false).orElseThrow(
                () -> new ExceptionAlert("The skill entered was not found")
        );

        skillUpdate.setSkillName(skill.getSkillName().toUpperCase());

        skillRepositoryPort.findBySkillNameAndSkillDeleted(skillUpdate.getSkillName(), false).ifPresent(
                e ->{
                    if (!Objects.equals(e.getSkillId(), skillUpdate.getSkillId())){
                        throw new ExceptionAlert("The name of the skill to be updated is already registered");
                    }
                }
        );

        skillUpdate.setSkillDeleted(false);

        return skillRepositoryPort.updateSkill(skillUpdate);
    }

}
