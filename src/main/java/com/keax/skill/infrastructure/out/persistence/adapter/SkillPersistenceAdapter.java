package com.keax.skill.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;

import com.keax.skill.infrastructure.out.persistence.mapper.SkillPersistenceMapper;
import com.keax.skill.infrastructure.out.persistence.repository.JpaSkillRepository;
import com.keax.skill.infrastructure.out.persistence.entity.SkillEntity;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import org.springframework.stereotype.Repository;
import com.keax.skill.domain.model.Skill;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SkillPersistenceAdapter implements SkillRepositoryPort {
    private final JpaSkillRepository jpaSkillRepository;

    @Override
    public Skill createSkill(Skill skill) {
        SkillEntity saved = jpaSkillRepository.save(
                SkillPersistenceMapper.toEntity(skill)
        );
        return SkillPersistenceMapper.toDomain(saved);
    }

    @Override
    public Skill updateSkill(Skill skill) {
        SkillEntity updated = jpaSkillRepository.save(
                SkillPersistenceMapper.toEntity(skill)
        );
        return SkillPersistenceMapper.toDomain(updated);
    }

    @Override
    public Skill deleteSkill(Skill skill) {
        SkillEntity deleted = jpaSkillRepository.save(
                SkillPersistenceMapper.toEntity(skill)
        );
        return SkillPersistenceMapper.toDomain(deleted);
    }

    @Override
    public List<Skill> findBySkillDeleted(Boolean deleted) {
        return jpaSkillRepository.findBySkillDeleted(deleted).stream()
                .map(SkillPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Skill> getListSkill() {
        return jpaSkillRepository.findBySkillDeleted(false).stream()
                .map(SkillPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Skill> findBySkillNameAndSkillDeleted(String skillName, Boolean deleted) {
        return jpaSkillRepository.findBySkillNameAndSkillDeleted(
                skillName,
                deleted
        ).map(SkillPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Skill> findBySkillIdAndSkillDeleted(Long skillId, Boolean deleted) {
        return jpaSkillRepository.findBySkillIdAndSkillDeleted(
                skillId,
                deleted
        ).map(SkillPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Skill> findBySkillPositionAndSkillDeleted(int position, Boolean deleted) {
        return jpaSkillRepository.findBySkillPositionAndSkillDeleted(
                position,
                deleted
        ).map(SkillPersistenceMapper::toDomain);
    }

}
