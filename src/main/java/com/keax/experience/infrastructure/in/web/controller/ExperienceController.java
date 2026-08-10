package com.keax.experience.infrastructure.in.web.controller;

import com.keax.experience.domain.ports.in.CreateExperienceUseCase;
import com.keax.experience.domain.ports.in.DeleteExperienceUseCase;
import com.keax.experience.domain.ports.in.RetrieveExperienceUseCase;
import com.keax.experience.domain.ports.in.UpdateExperienceUseCase;
import com.keax.experience.infrastructure.in.web.dto.ExperienceDTO;
import com.keax.experience.infrastructure.in.web.mapper.ExperienceWebMapper;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/experience")
@RequiredArgsConstructor
public class ExperienceController {

    private final CreateExperienceUseCase createExperience;
    private final UpdateExperienceUseCase updateExperience;
    private final RetrieveExperienceUseCase retrieveExperience;
    private final DeleteExperienceUseCase deleteExperience;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ExperienceDTO>> create(
            @Valid @RequestBody ExperienceDTO experience
    ) {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "The experience was created successfully",
                ExperienceWebMapper.fromDomain(
                        createExperience.createExperience(ExperienceWebMapper.toDomain(experience))
                )
        ));
    }

    @PutMapping("/{experienceId}")
    public ResponseEntity<ApiResponseDTO<ExperienceDTO>> update(
            @PathVariable Long experienceId,
            @Valid @RequestBody ExperienceDTO experience
    ) {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "The experience was updated successfully",
                ExperienceWebMapper.fromDomain(updateExperience.updateExperience(
                        experienceId,
                        ExperienceWebMapper.toDomain(experience)
                ))
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ExperienceDTO>>> list() {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "The experiences were found successfully",
                retrieveExperience.getExperiences().stream()
                        .map(ExperienceWebMapper::fromDomain)
                        .toList()
        ));
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<ApiResponseDTO<ExperienceDTO>> delete(
            @PathVariable Long experienceId
    ) {
        deleteExperience.deleteExperience(experienceId);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "The experience was deleted successfully",
                null
        ));
    }
}
