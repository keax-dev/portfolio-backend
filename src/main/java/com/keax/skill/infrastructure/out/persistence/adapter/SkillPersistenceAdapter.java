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
        jpaSkillRepository.deleteById(skill.getSkillId());
        jpaSkillRepository.flush();
        return skill;
    }

    @Override
    public List<Skill> findAll() {
        return jpaSkillRepository.findAllByOrderBySkillPositionAsc().stream()
                .map(SkillPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Skill> findVisible() {
        return jpaSkillRepository.findAllBySkillVisibleTrueOrderBySkillPositionAsc().stream()
                .map(SkillPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Skill> findByName(String skillName) {
        return jpaSkillRepository.findBySkillName(skillName)
                .map(SkillPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Skill> findById(Long skillId) {
        return jpaSkillRepository.findById(skillId)
                .map(SkillPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Skill> findByPosition(int position) {
        return jpaSkillRepository.findBySkillPosition(position)
                .map(SkillPersistenceMapper::toDomain);
    }

}
