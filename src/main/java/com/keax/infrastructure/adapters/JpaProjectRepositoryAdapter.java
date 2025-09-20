package com.keax.infrastructure.adapters;

import com.keax.infrastructure.repositories.JpaProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.entities.TechnologyEntity;
import com.keax.domain.ports.out.ProjectRepositoryPort;
import com.keax.infrastructure.entities.ProjectEntity;
import org.springframework.stereotype.Repository;
import com.keax.domain.models.Project;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

@Repository
public class JpaProjectRepositoryAdapter implements ProjectRepositoryPort {

    @Autowired
    private JpaProjectRepository jpaProjectRepository;

    @Override
    public Project createProject(Project project) {
        ProjectEntity saved = jpaProjectRepository.save(fromDomainModel(project));
        return toDomainModel(saved);
    }

    @Override
    public Project updateProject(Project project) {
        ProjectEntity update = jpaProjectRepository.save(fromDomainModel(project));
        return toDomainModel(update);
    }

    @Override
    public Project deleteProject(Project project) {
        ProjectEntity deleted = jpaProjectRepository.save(fromDomainModel(project));
        return toDomainModel(deleted);
    }

    @Override
    public List<Project> findByProjectDeleted(Boolean deleted) {
        return jpaProjectRepository.findByProjectDeleted(deleted).stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public List<Project> getListProject() {
        return jpaProjectRepository.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Project> findByProjectTittleAndProjectDeleted(String projectTittle, Boolean deleted) {
        return jpaProjectRepository.findByProjectTittleAndProjectDeleted(projectTittle, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<Project> findByProjectIdAndProjectDeleted(Long projectId, Boolean deleted) {
        return jpaProjectRepository.findByProjectIdAndProjectDeleted(projectId, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<Project> findByProjectPositionAndProjectDeletedAndTechnology_technologyId(int position, Boolean deleted, Long technologyId) {
        return jpaProjectRepository.findByProjectPositionAndProjectDeletedAndTechnology_technologyId(position, deleted, technologyId).map(this::toDomainModel);
    }

    private Project toDomainModel(ProjectEntity projectEntity){
        return new Project(
                projectEntity.getProjectId(),
                projectEntity.getProjectTittle(),
                projectEntity.getProjectDescription(),
                projectEntity.getProjectPicture(),
                projectEntity.getProjectDeploy(),
                projectEntity.getProjectGithub(),
                projectEntity.getProjectPosition(),
                projectEntity.getTechnology().getTechnologyId(),
                projectEntity.getTechnology().getTechnologyName(),
                projectEntity.getProjectDeleted()
        );
    }

    private ProjectEntity fromDomainModel(Project project){

        TechnologyEntity technology = new TechnologyEntity();
        technology.setTechnologyId(project.getTechnologyId());

        return new ProjectEntity(
                project.getProjectId(),
                project.getProjectTittle(),
                project.getProjectDescription(),
                project.getProjectPicture(),
                project.getProjectDeploy(),
                project.getProjectGithub(),
                project.getProjectPosition(),
                project.getProjectDeleted(),
                technology
        );
    }

}
