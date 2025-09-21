package com.keax.infrastructure.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.repositories.JpaSkillRepository;
import com.keax.domain.ports.out.SkillRepositoryPort;
import com.keax.infrastructure.entities.SkillEntity;
import org.springframework.stereotype.Repository;
import com.keax.domain.models.Skill;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

@Repository
public class JpaSkillRepositoryAdapter implements SkillRepositoryPort {

    @Autowired
    private JpaSkillRepository jpaSkillRepository;

    @Override
    public Skill createSkill(Skill skill) {
        SkillEntity saved = jpaSkillRepository.save(fromDomainModel(skill));
        return toDomainModel(saved);
    }

    @Override
    public Skill updateSkill(Skill skill) {
        SkillEntity update = jpaSkillRepository.save(fromDomainModel(skill));
        return toDomainModel(update);
    }

    @Override
    public Skill deleteSkill(Skill skill) {
        SkillEntity deleted = jpaSkillRepository.save(fromDomainModel(skill));
        return toDomainModel(deleted);
    }

    @Override
    public List<Skill> findBySkillDeleted(Boolean deleted) {
        return jpaSkillRepository.findBySkillDeleted(deleted).stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public List<Skill> getListSkill() {
        return jpaSkillRepository.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Skill> findBySkillNameAndSkillDeleted(String skillName, Boolean deleted) {
        return jpaSkillRepository.findBySkillNameAndSkillDeleted(skillName, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<Skill> findBySkillIdAndSkillDeleted(Long skillId, Boolean deleted) {
        return jpaSkillRepository.findBySkillIdAndSkillDeleted(skillId, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<Skill> findBySkillPositionAndSkillDeleted(int position, Boolean deleted) {
        return jpaSkillRepository.findBySkillPositionAndSkillDeleted(position, deleted).map(this::toDomainModel);
    }

    private Skill toDomainModel(SkillEntity skillEntity){
        return new Skill(skillEntity.getSkillId(), skillEntity.getSkillName(), skillEntity.getSkillPicture(), skillEntity.getSkillPosition(), skillEntity.getSkillDeleted());
    }

    private SkillEntity fromDomainModel(Skill skill){
        return new SkillEntity(skill.getSkillId(), skill.getSkillName(), skill.getSkillPicture(), skill.getSkillPosition(), skill.getSkillDeleted());
    }

}
