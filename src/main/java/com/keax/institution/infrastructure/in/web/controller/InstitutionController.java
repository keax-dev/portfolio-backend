package com.keax.institution.infrastructure.in.web.controller;

import lombok.RequiredArgsConstructor;

import com.keax.institution.infrastructure.in.web.mapper.InstitutionWebMapper;
import com.keax.institution.domain.ports.in.RetrieveInstitutionUseCase;
import com.keax.institution.domain.ports.in.CreateInstitutionUseCase;
import com.keax.institution.domain.ports.in.DeleteInstitutionUseCase;
import com.keax.institution.domain.ports.in.UpdateInstitutionUseCase;
import com.keax.institution.infrastructure.in.web.dto.InstitutionDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/institution")
@RequiredArgsConstructor
public class InstitutionController {
    private final CreateInstitutionUseCase createInstitutionUseCase;
    private final UpdateInstitutionUseCase updateInstitutionUseCase;
    private final RetrieveInstitutionUseCase retrieveInstitutionUseCase;
    private final DeleteInstitutionUseCase deleteInstitutionUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<InstitutionDTO>> create(@Valid @RequestBody InstitutionDTO institution) {
        ApiResponseDTO<InstitutionDTO> response = new ApiResponseDTO<>(
                true,
                "The institution was successfully created",
                InstitutionWebMapper.fromDomain(
                        createInstitutionUseCase.createInstitution(InstitutionWebMapper.toDomain(institution))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{institution_id}")
    public ResponseEntity<ApiResponseDTO<InstitutionDTO>> update(@PathVariable Long institution_id, @Valid @RequestBody InstitutionDTO institution) {
        ApiResponseDTO<InstitutionDTO> response = new ApiResponseDTO<>(
                true,
                "The institution was successfully updated",
                InstitutionWebMapper.fromDomain(
                        updateInstitutionUseCase.updateInstitution(institution_id, InstitutionWebMapper.toDomain(institution))
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<InstitutionDTO>>> list() {
        ApiResponseDTO<List<InstitutionDTO>> response = new ApiResponseDTO<>(
                true,
                "The institutions were successfully found",
                retrieveInstitutionUseCase.getListInstitution().stream().map(InstitutionWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{institution_id}")
    public ResponseEntity<ApiResponseDTO<InstitutionDTO>> delete(@PathVariable Long institution_id) {
        deleteInstitutionUseCase.deleteInstitution(institution_id);

        ApiResponseDTO<InstitutionDTO> response = new ApiResponseDTO<>(
                true,
                "The institution was successfully eliminated",
                null
        );

        return ResponseEntity.ok(response);
    }

}
