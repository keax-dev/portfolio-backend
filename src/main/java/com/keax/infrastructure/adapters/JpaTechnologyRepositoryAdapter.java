package com.keax.infrastructure.adapters;

import com.keax.infrastructure.repositories.JpaTechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.infrastructure.entities.TechnologyEntity;
import org.springframework.stereotype.Repository;
import com.keax.domain.models.Technology;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

@Repository
public class JpaTechnologyRepositoryAdapter implements TechnologyRepositoryPort {

    @Autowired
    private JpaTechnologyRepository jpaTechnologyRepository;

    @Override
    public Technology createTechnology(Technology technology) {
        TechnologyEntity saved = jpaTechnologyRepository.save(fromDomainModel(technology));
        return toDomainModel(saved);
    }

    @Override
    public Technology updateTechnology(Technology technology) {
        TechnologyEntity update = jpaTechnologyRepository.save(fromDomainModel(technology));
        return toDomainModel(update);
    }

    @Override
    public Technology deleteTechnology(Technology technology) {
        TechnologyEntity deleted = jpaTechnologyRepository.save(fromDomainModel(technology));
        return toDomainModel(deleted);
    }

    @Override
    public List<Technology> findByTechnologyDeleted(Boolean deleted) {
        return jpaTechnologyRepository.findByTechnologyDeleted(deleted).stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public List<Technology> getListTechnology() {
        return jpaTechnologyRepository.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Technology> findByTechnologyNameAndTechnologyDeleted(String technologyName, Boolean deleted) {
        return jpaTechnologyRepository.findByTechnologyNameAndTechnologyDeleted(technologyName, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<Technology> findByTechnologyIdAndTechnologyDeleted(Long technologyId, Boolean deleted) {
        return jpaTechnologyRepository.findByTechnologyIdAndTechnologyDeleted(technologyId, deleted).map(this::toDomainModel);
    }

    private Technology toDomainModel(TechnologyEntity technologyEntity){
        return new Technology(technologyEntity.getTechnologyId(), technologyEntity.getTechnologyName(), technologyEntity.getTechnologyDeleted());
    }

    private TechnologyEntity fromDomainModel(Technology technology){
        return new TechnologyEntity(technology.getTechnologyId(), technology.getTechnologyName(), technology.getTechnologyDeleted());
    }

}
