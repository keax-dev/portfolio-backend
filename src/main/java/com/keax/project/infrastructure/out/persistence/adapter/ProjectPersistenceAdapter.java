package com.keax.project.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;

import com.keax.project.infrastructure.out.persistence.mapper.ProjectPersistenceMapper;
import com.keax.project.infrastructure.out.persistence.repository.JpaProjectRepository;
import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import org.springframework.stereotype.Repository;
import com.keax.project.domain.model.Project;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {
    private final JpaProjectRepository jpaProjectRepository;

    @Override
    public Project createProject(Project project) {
        ProjectEntity saved = jpaProjectRepository.save(
                ProjectPersistenceMapper.toEntity(project)
        );
        return ProjectPersistenceMapper.toDomain(saved);
    }

    @Override
    public Project updateProject(Project project) {
        ProjectEntity updated = jpaProjectRepository.save(
                ProjectPersistenceMapper.toEntity(project)
        );
        return ProjectPersistenceMapper.toDomain(updated);
    }

    @Override
    public Project deleteProject(Project project) {
        ProjectEntity deleted = jpaProjectRepository.save(
                ProjectPersistenceMapper.toEntity(project)
        );
        return ProjectPersistenceMapper.toDomain(deleted);
    }

    @Override
    public List<Project> findByProjectDeleted(Boolean deleted) {
        return jpaProjectRepository.findByProjectDeleted(deleted).stream()
                .map(ProjectPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Project> getListProject() {
        return jpaProjectRepository.findByProjectDeleted(false).stream()
                .map(ProjectPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Project> findByProjectTitleAndProjectDeleted(String projectTitle, Boolean deleted) {
        return jpaProjectRepository.findByProjectTitleAndProjectDeleted(
                projectTitle,
                deleted
        ).map(ProjectPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Project> findByProjectIdAndProjectDeleted(Long projectId, Boolean deleted) {
        return jpaProjectRepository.findByProjectIdAndProjectDeleted(
                projectId,
                deleted
        ).map(ProjectPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Project> findByProjectPositionAndProjectDeletedAndTechnology_technologyId(int position, Boolean deleted, Long technologyId) {
        return jpaProjectRepository.findByProjectPositionAndProjectDeletedAndTechnology_technologyId(
                position,
                deleted,
                technologyId
        ).map(ProjectPersistenceMapper::toDomain);
    }

    @Override
    public Boolean existsByTechnology_technologyIdAndProjectDeleted(Long technologyId, Boolean deleted) {
        return jpaProjectRepository.existsByTechnology_technologyIdAndProjectDeleted(
                technologyId,
                deleted
        );
    }

}
