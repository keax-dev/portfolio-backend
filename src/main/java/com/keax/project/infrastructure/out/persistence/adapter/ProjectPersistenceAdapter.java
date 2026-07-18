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
        jpaProjectRepository.stageProjectTechnologyPositions(project.getProjectId());
        jpaProjectRepository.stageProjectLinkPositions(project.getProjectId());
        ProjectEntity updated = jpaProjectRepository.saveAndFlush(
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
        return jpaProjectRepository.findByProjectDeletedOrderByProjectPosition(deleted).stream()
                .map(ProjectPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Project> getListProject() {
        return jpaProjectRepository.findByProjectDeletedOrderByProjectPosition(false).stream()
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
    public Optional<Project> findByProjectPositionAndProjectDeleted(int position, Boolean deleted) {
        return jpaProjectRepository.findByProjectPositionAndProjectDeleted(
                position,
                deleted
        ).map(ProjectPersistenceMapper::toDomain);
    }

    @Override
    public Boolean existsByTechnologyIdAndProjectDeleted(Long technologyId, Boolean deleted) {
        return jpaProjectRepository.existsByTechnologyIdAndProjectDeleted(
                technologyId,
                deleted
        );
    }

}
