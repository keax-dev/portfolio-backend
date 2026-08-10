package com.keax.skill.infrastructure.out.persistence.mapper;

import com.keax.skill.infrastructure.out.persistence.entity.SkillEntity;
import com.keax.skill.domain.model.Skill;

public final class SkillPersistenceMapper {

    public static Skill toDomain(SkillEntity entity) {
        return new Skill(
                entity.getSkillId(),
                entity.getSkillName(),
                entity.getSkillPicture(),
                entity.getSkillPosition(),
                entity.getSkillCategory(),
                entity.getSkillVisible(),
                entity.getVersion()
        );
    }

    public static SkillEntity toEntity(Skill skill) {
        return new SkillEntity(
                skill.getSkillId(),
                skill.getSkillName(),
                skill.getSkillPicture(),
                skill.getSkillPosition(),
                skill.getSkillCategory(),
                skill.getSkillVisible(),
                false,
                skill.getVersion()
        );
    }

}
