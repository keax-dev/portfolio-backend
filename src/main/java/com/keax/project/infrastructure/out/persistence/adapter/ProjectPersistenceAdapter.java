package com.keax.project.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;

import com.keax.project.infrastructure.out.persistence.mapper.ProjectPersistenceMapper;
import com.keax.project.infrastructure.out.persistence.repository.JpaProjectRepository;
import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import com.keax.project.domain.model.Project;
import java.util.Optional;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {
    private final JpaProjectRepository jpaProjectRepository;
    private final EntityManager entityManager;

    @Override
    public Project createProject(Project project) {
        ProjectEntity saved = jpaProjectRepository.save(toEntity(project));
        return ProjectPersistenceMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Project updateProject(Project project) {
        jpaProjectRepository.stageProjectTechnologyPositions(project.getProjectId());
        jpaProjectRepository.stageProjectLinkPositions(project.getProjectId());
        ProjectEntity updated = jpaProjectRepository.saveAndFlush(toEntity(project));
        return ProjectPersistenceMapper.toDomain(updated);
    }

    @Override
    public Project deleteProject(Project project) {
        jpaProjectRepository.deleteById(project.getProjectId());
        jpaProjectRepository.flush();
        return project;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findPublished() {
        return jpaProjectRepository.findByProjectPublishedTrueOrderByProjectPosition().stream()
                .map(ProjectPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAll() {
        return jpaProjectRepository.findAllByOrderByProjectPosition().stream()
                .map(ProjectPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findByTitle(String projectTitle) {
        return jpaProjectRepository.findByProjectTitle(projectTitle)
                .map(ProjectPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findById(Long projectId) {
        return jpaProjectRepository.findById(projectId)
                .map(ProjectPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Project> findByPosition(int position) {
        return jpaProjectRepository.findByProjectPosition(position)
                .map(ProjectPersistenceMapper::toDomain);
    }

    private ProjectEntity toEntity(Project project) {
        ProjectEntity entity = ProjectPersistenceMapper.toEntity(project);
        entity.getProjectTechnologies().forEach(relation ->
                relation.setTechnology(entityManager.getReference(
                        TechnologyEntity.class,
                        relation.getTechnology().getTechnologyId()
                ))
        );
        return entity;
    }

}
