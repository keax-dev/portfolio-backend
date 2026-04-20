package com.keax.skill.infrastructure.in.web.mapper;

import com.keax.skill.infrastructure.in.web.dto.SkillDTO;
import com.keax.skill.domain.model.Skill;

public final class SkillWebMapper {

    public static Skill toDomain(SkillDTO dto) {
        return new Skill(
                dto.getSkillId(),
                dto.getSkillName(),
                dto.getSkillPicture(),
                dto.getSkillPosition(),
                dto.getSkillDeleted()
        );
    }

    public static SkillDTO fromDomain(Skill skill) {
        return new SkillDTO(
                skill.getSkillId(),
                skill.getSkillName(),
                skill.getSkillPicture(),
                skill.getSkillPosition(),
                skill.getSkillDeleted()
        );
    }

}
