package com.keax.application.services.Implementation;

import com.keax.application.services.Interfaces.ISkillService;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Skill.RetrieveSkillUseCase;
import com.keax.domain.ports.in.Skill.CreateSkillUseCase;
import com.keax.domain.ports.in.Skill.DeleteSkillUseCase;
import com.keax.domain.ports.in.Skill.UpdateSkillUseCase;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Skill;
import java.util.List;

@Service
public class SkillService implements ISkillService {

    @Autowired
    private CreateSkillUseCase createSkillUseCase;

    @Autowired
    private UpdateSkillUseCase updateSkillUseCase;

    @Autowired
    private DeleteSkillUseCase deleteSkillUseCase;

    @Autowired
    private RetrieveSkillUseCase retrieveSkillUseCase;

    @Override
    public Skill createSkill(Skill skill) {
        return createSkillUseCase.createSkill(skill);
    }

    @Override
    public Skill updateSkill(Long skillId, Skill skill) {
        return updateSkillUseCase.updateSkill(skillId, skill);
    }

    @Override
    public Skill deleteSkill(Long skillId) {
        return deleteSkillUseCase.deleteSkill(skillId);
    }

    @Override
    public List<Skill> findBySkillDeleted(Boolean deleted) {
        return retrieveSkillUseCase.findBySkillDeleted(deleted);
    }

    @Override
    public List<Skill> getListSkill() {
        return retrieveSkillUseCase.getListSkill();
    }

}
