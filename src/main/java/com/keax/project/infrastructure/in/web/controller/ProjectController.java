package com.keax.project.infrastructure.in.web.controller;

import lombok.RequiredArgsConstructor;

import com.keax.project.infrastructure.in.web.mapper.ProjectWebMapper;
import com.keax.project.domain.ports.in.RetrieveProjectUseCase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keax.project.domain.ports.in.CreateProjectUseCase;
import com.keax.project.domain.ports.in.DeleteProjectUseCase;
import com.keax.project.domain.ports.in.UpdateProjectUseCase;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectDTO;
import com.keax.project.infrastructure.in.web.dto.CreateProjectRequestDTO;
import com.keax.project.infrastructure.in.web.dto.UpdateProjectRequestDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;
import java.net.URI;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    private final CreateProjectUseCase createProjectUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final RetrieveProjectUseCase retrieveProjectUseCase;
    private final DeleteProjectUseCase deleteProjectUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProjectDTO>> create(@Valid @RequestBody CreateProjectRequestDTO project) {
        ProjectDTO createdProject = ProjectWebMapper.fromDomain(
                createProjectUseCase.createProject(ProjectWebMapper.toDomain(project))
        );
        ApiResponseDTO<ProjectDTO> response = new ApiResponseDTO<>(
                true,
                "The project was created successfully",
                createdProject
        );

        return ResponseEntity.created(URI.create("/api/project/" + createdProject.getProjectId())).body(response);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponseDTO<ProjectDTO>> update(@PathVariable Long projectId, @Valid @RequestBody UpdateProjectRequestDTO project) {
        ApiResponseDTO<ProjectDTO> response = new ApiResponseDTO<>(
                true,
                "The project was successfully updated",
                ProjectWebMapper.fromDomain(
                        updateProjectUseCase.updateProject(projectId, ProjectWebMapper.toDomain(project))
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ProjectDTO>>> list() {
        ApiResponseDTO<List<ProjectDTO>> response = new ApiResponseDTO<>(
                true,
                "The projects were found successfully",
                retrieveProjectUseCase.getListProject().stream().map(ProjectWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> delete(@PathVariable Long projectId) {
        deleteProjectUseCase.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

}
