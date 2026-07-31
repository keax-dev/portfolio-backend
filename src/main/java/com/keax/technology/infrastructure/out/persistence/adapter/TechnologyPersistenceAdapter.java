package com.keax.technology.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;

import com.keax.technology.infrastructure.out.persistence.mapper.TechnologyPersistenceMapper;
import com.keax.technology.infrastructure.out.persistence.repository.JpaTechnologyRepository;
import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import com.keax.technology.domain.model.Technology;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TechnologyPersistenceAdapter implements TechnologyRepositoryPort {
    private final JpaTechnologyRepository jpaTechnologyRepository;

    @Override
    public Technology createTechnology(Technology technology) {
        TechnologyEntity saved = jpaTechnologyRepository.save(
                TechnologyPersistenceMapper.toEntity(technology)
        );
        return TechnologyPersistenceMapper.toDomain(saved);
    }

    @Override
    public Technology updateTechnology(Technology technology) {
        TechnologyEntity updated = jpaTechnologyRepository.save(
                TechnologyPersistenceMapper.toEntity(technology)
        );
        return TechnologyPersistenceMapper.toDomain(updated);
    }

    @Override
    public Technology deleteTechnology(Technology technology) {
        jpaTechnologyRepository.deleteById(technology.getTechnologyId());
        jpaTechnologyRepository.flush();
        return technology;
    }

    @Override
    public List<Technology> findAll() {
        return jpaTechnologyRepository.findAllByOrderByTechnologyNameAsc()
                .stream()
                .map(TechnologyPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Technology> findByName(String technologyName) {
        return jpaTechnologyRepository.findByTechnologyName(technologyName)
                .map(TechnologyPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Technology> findById(Long technologyId) {
        return jpaTechnologyRepository.findById(technologyId)
                .map(TechnologyPersistenceMapper::toDomain);
    }

}
