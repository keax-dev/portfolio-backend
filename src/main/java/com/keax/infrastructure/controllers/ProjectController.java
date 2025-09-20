package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.keax.domain.models.Project;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse<Project>> create(@Valid @RequestBody Project project){

        ApiResponse<Project> response = new ApiResponse<>(
                true,
                "The project was created successfully",
                projectService.createProject(project)
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Project>> update(@PathVariable Long projectId, @Valid @RequestBody Project project){

        ApiResponse<Project> response = new ApiResponse<>(
                true,
                "The project was successfully updated",
                projectService.updateProject(projectId, project)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Project>>> list(){

        ApiResponse<List<Project>> response = new ApiResponse<>(
                true,
                "The projects were found successfully",
                projectService.getListProject()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-deleted/{deleted}")
    public ResponseEntity<ApiResponse<List<Project>>> listByDeleted(@PathVariable Boolean deleted){

        ApiResponse<List<Project>> response = new ApiResponse<>(
                true,
                "The skills were found successfully",
                projectService.findByProjectDeleted(deleted)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Project>> delete(@PathVariable Long projectId){

        Project project = projectService.deleteProject(projectId);

        ApiResponse<Project> response = new ApiResponse<>(
                true,
                "The project was successfully deleted",
                null
        );

        return ResponseEntity.ok(response);
    }

}
