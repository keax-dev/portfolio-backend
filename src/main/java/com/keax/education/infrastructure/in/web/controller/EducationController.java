package com.keax.education.infrastructure.in.web.controller;

import com.keax.education.infrastructure.in.web.mapper.EducationWebMapper;
import com.keax.education.domain.ports.in.RetrieveEducationUseCase;
import com.keax.education.domain.ports.in.CreateEducationUseCase;
import com.keax.education.domain.ports.in.DeleteEducationUseCase;
import com.keax.education.domain.ports.in.UpdateEducationUseCase;
import com.keax.education.infrastructure.in.web.dto.EducationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    @Autowired
    private CreateEducationUseCase createEducationUseCase;

    @Autowired
    private UpdateEducationUseCase updateEducationUseCase;

    @Autowired
    private RetrieveEducationUseCase retrieveEducationUseCase;

    @Autowired
    private DeleteEducationUseCase deleteEducationUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<EducationDTO>> create(@Valid @RequestBody EducationDTO education) {
        ApiResponseDTO<EducationDTO> response = new ApiResponseDTO<>(
                true,
                "Education was created successfully",
                EducationWebMapper.fromDomain(
                        createEducationUseCase.createEducation(EducationWebMapper.toDomain(education))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{education_id}")
    public ResponseEntity<ApiResponseDTO<EducationDTO>> update(@PathVariable Long education_id, @Valid @RequestBody EducationDTO education) {
        ApiResponseDTO<EducationDTO> response = new ApiResponseDTO<>(
                true,
                "Education was successfully updated",
                EducationWebMapper.fromDomain(
                        updateEducationUseCase.updateEducation(education_id, EducationWebMapper.toDomain(education))
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<EducationDTO>>> list() {
        ApiResponseDTO<List<EducationDTO>> response = new ApiResponseDTO<>(
                true,
                "The educations were found successfully",
                retrieveEducationUseCase.getListEducation().stream().map(EducationWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-deleted/{deleted}")
    public ResponseEntity<ApiResponseDTO<List<EducationDTO>>> listByDeleted(@PathVariable Boolean deleted) {
        ApiResponseDTO<List<EducationDTO>> response = new ApiResponseDTO<>(
                true,
                "The educations were found successfully",
                retrieveEducationUseCase.findByEducationDeleted(deleted).stream().map(EducationWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{education_id}")
    public ResponseEntity<ApiResponseDTO<EducationDTO>> delete(@PathVariable Long education_id) {
        deleteEducationUseCase.deleteEducation(education_id);

        ApiResponseDTO<EducationDTO> response = new ApiResponseDTO<>(
                true,
                "Education is successfully eliminated",
                null
        );

        return ResponseEntity.ok(response);
    }

}
