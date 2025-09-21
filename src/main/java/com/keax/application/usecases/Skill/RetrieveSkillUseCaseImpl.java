package com.keax.application.usecases.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Skill.RetrieveSkillUseCase;
import com.keax.domain.ports.out.SkillRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Skill;
import java.util.List;

@Component
public class RetrieveSkillUseCaseImpl implements RetrieveSkillUseCase {

    @Autowired
    private SkillRepositoryPort skillRepositoryPort;

    @Override
    public List<Skill> findBySkillDeleted(Boolean deleted) {

        List<Skill> skillList = skillRepositoryPort.findBySkillDeleted(deleted);

        return validateNotEmpty(skillList);
    }

    @Override
    public List<Skill> getListSkill() {

        List<Skill> skillList = skillRepositoryPort.getListSkill();

        return validateNotEmpty(skillList);
    }

    private List<Skill> validateNotEmpty(List<Skill> skillList) {

        if (skillList.isEmpty()) {
            throw new ExceptionAlert("There are no created skills");
        }

        return skillList;
    }

}
