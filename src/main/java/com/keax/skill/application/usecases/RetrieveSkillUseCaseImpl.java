package com.keax.skill.application.usecases;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;
import com.keax.skill.domain.ports.in.RetrieveSkillUseCase;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Service;
import com.keax.skill.domain.model.Skill;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrieveSkillUseCaseImpl implements RetrieveSkillUseCase {
    private final SkillRepositoryPort skillRepositoryPort;

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
