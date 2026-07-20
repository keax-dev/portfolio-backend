package com.keax.skill.application.usecases;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;
import com.keax.skill.domain.ports.in.RetrieveSkillUseCase;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
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

        return skillRepositoryPort.findBySkillDeleted(deleted);
    }

    @Override
    public List<Skill> getListSkill() {

        return skillRepositoryPort.getListSkill();
    }

}
