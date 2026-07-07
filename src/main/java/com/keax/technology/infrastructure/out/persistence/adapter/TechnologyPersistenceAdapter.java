package com.keax.technology.infrastructure.out.persistence.adapter;

import com.keax.technology.infrastructure.out.persistence.mapper.TechnologyPersistenceMapper;
import com.keax.technology.infrastructure.out.persistence.repository.JpaTechnologyRepository;
import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.technology.domain.model.Technology;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public class TechnologyPersistenceAdapter implements TechnologyRepositoryPort {

    @Autowired
    private JpaTechnologyRepository jpaTechnologyRepository;

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
        TechnologyEntity deleted = jpaTechnologyRepository.save(
                TechnologyPersistenceMapper.toEntity(technology)
        );
        return TechnologyPersistenceMapper.toDomain(deleted);
    }

    @Override
    public List<Technology> findByTechnologyDeleted(Boolean deleted) {
        return jpaTechnologyRepository.findByTechnologyDeleted(deleted)
                .stream()
                .map(TechnologyPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Technology> findByTechnologyDeletedWithProjects(Boolean deleted) {
        return jpaTechnologyRepository.findWithProjectsByTechnologyDeleted(deleted)
                .stream()
                .map(TechnologyPersistenceMapper::toDomainWithProjects)
                .toList();
    }

    @Override
    public List<Technology> getListTechnology() {
        return jpaTechnologyRepository.findByTechnologyDeleted(false)
                .stream()
                .map(TechnologyPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Technology> findByTechnologyNameAndTechnologyDeleted(String technologyName, Boolean deleted) {
        return jpaTechnologyRepository.findByTechnologyNameAndTechnologyDeleted(
                technologyName,
                deleted
        ).map(TechnologyPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Technology> findByTechnologyIdAndTechnologyDeleted(Long technologyId, Boolean deleted) {
        return jpaTechnologyRepository.findByTechnologyIdAndTechnologyDeleted(
                technologyId,
                deleted
        ).map(TechnologyPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Technology> findByTechnologyPositionAndTechnologyDeleted(int position, Boolean deleted) {
        return jpaTechnologyRepository.findByTechnologyPositionAndTechnologyDeleted(
                position,
                deleted
        ).map(TechnologyPersistenceMapper::toDomain);
    }

}
