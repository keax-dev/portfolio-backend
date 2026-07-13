package com.keax.technology.infrastructure.in.web.controller;

import lombok.RequiredArgsConstructor;

import com.keax.technology.infrastructure.in.web.mapper.TechnologyWebMapper;
import com.keax.technology.domain.ports.in.RetrieveTechnologyUseCase;
import com.keax.technology.domain.ports.in.UpdateTechnologyUseCase;
import com.keax.technology.domain.ports.in.CreateTechnologyUseCase;
import com.keax.technology.domain.ports.in.DeleteTechnologyUseCase;
import com.keax.technology.infrastructure.in.web.dto.TechnologyDTO;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/api/technology")
@RequiredArgsConstructor
public class TechnologyController {
    private final CreateTechnologyUseCase createTechnologyUseCase;
    private final UpdateTechnologyUseCase updateTechnologyUseCase;
    private final RetrieveTechnologyUseCase retrieveTechnologyUseCase;
    private final DeleteTechnologyUseCase deleteTechnologyUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TechnologyDTO>> create(@Valid @RequestBody TechnologyDTO technology) {
        ApiResponseDTO<TechnologyDTO> response = new ApiResponseDTO<>(
                true,
                "The technology was created successfully",
                TechnologyWebMapper.fromDomain(
                        createTechnologyUseCase.createTechnology(TechnologyWebMapper.toDomain(technology))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{technologyId}")
    public ResponseEntity<ApiResponseDTO<TechnologyDTO>> update(@PathVariable Long technologyId, @Valid @RequestBody TechnologyDTO technology) {
        ApiResponseDTO<TechnologyDTO> response = new ApiResponseDTO<>(
                true,
                "The technology was successfully updated",
                TechnologyWebMapper.fromDomain(
                        updateTechnologyUseCase.updateTechnology(technologyId, TechnologyWebMapper.toDomain(technology))
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TechnologyDTO>>> list() {
        ApiResponseDTO<List<TechnologyDTO>> response = new ApiResponseDTO<>(
                true,
                "The technologies were found successfully",
                retrieveTechnologyUseCase.getListTechnology().stream().map(TechnologyWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{technologyId}")
    public ResponseEntity<ApiResponseDTO<TechnologyDTO>> delete(@PathVariable Long technologyId) {
        deleteTechnologyUseCase.deleteTechnology(technologyId);

        ApiResponseDTO<TechnologyDTO> response = new ApiResponseDTO<>(
                true,
                "The technology is successfully eliminated",
                null
        );

        return ResponseEntity.ok(response);
    }

}
