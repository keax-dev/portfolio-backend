package com.keax.project.infrastructure.in.web.controller;

import com.keax.project.infrastructure.in.web.mapper.ProjectWebMapper;
import com.keax.project.domain.ports.in.RetrieveProjectUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keax.project.domain.ports.in.CreateProjectUseCase;
import com.keax.project.domain.ports.in.DeleteProjectUseCase;
import com.keax.project.domain.ports.in.UpdateProjectUseCase;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private CreateProjectUseCase createProjectUseCase;

    @Autowired
    private UpdateProjectUseCase updateProjectUseCase;

    @Autowired
    private RetrieveProjectUseCase retrieveProjectUseCase;

    @Autowired
    private DeleteProjectUseCase deleteProjectUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProjectDTO>> create(@Valid @RequestBody ProjectDTO project) {
        ApiResponseDTO<ProjectDTO> response = new ApiResponseDTO<>(
                true,
                "The project was created successfully",
                ProjectWebMapper.fromDomain(
                        createProjectUseCase.createProject(ProjectWebMapper.toDomain(project))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponseDTO<ProjectDTO>> update(@PathVariable Long projectId, @Valid @RequestBody ProjectDTO project) {
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
    public ResponseEntity<ApiResponseDTO<ProjectDTO>> delete(@PathVariable Long projectId) {
        deleteProjectUseCase.deleteProject(projectId);

        ApiResponseDTO<ProjectDTO> response = new ApiResponseDTO<>(
                true,
                "The project was successfully deleted",
                null
        );

        return ResponseEntity.ok(response);
    }

}
