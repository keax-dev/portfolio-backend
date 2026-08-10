package com.keax.experience.infrastructure.out.persistence.adapter;

import com.keax.experience.domain.model.Experience;
import com.keax.experience.domain.ports.out.ExperienceRepositoryPort;
import com.keax.experience.infrastructure.out.persistence.mapper.ExperiencePersistenceMapper;
import com.keax.experience.infrastructure.out.persistence.repository.JpaExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExperiencePersistenceAdapter implements ExperienceRepositoryPort {

    private final JpaExperienceRepository repository;

    @Override
    public Experience save(Experience experience) {
        return ExperiencePersistenceMapper.toDomain(
                repository.save(ExperiencePersistenceMapper.toEntity(experience))
        );
    }

    @Override
    public Experience delete(Experience experience) {
        repository.deleteById(experience.getExperienceId());
        repository.flush();
        return experience;
    }

    @Override
    public List<Experience> findAll() {
        return repository.findAllByOrderByExperiencePositionAsc().stream()
                .map(ExperiencePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Experience> findVisible() {
        return repository.findAllByExperienceVisibleTrueOrderByExperiencePositionAsc().stream()
                .map(ExperiencePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Experience> findById(Long experienceId) {
        return repository.findById(experienceId).map(ExperiencePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Experience> findByPosition(int position) {
        return repository.findByExperiencePosition(position)
                .map(ExperiencePersistenceMapper::toDomain);
    }
}
