package com.keax.skill.infrastructure.in.web.controller;

import com.keax.skill.infrastructure.in.web.mapper.SkillWebMapper;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.keax.skill.domain.ports.in.RetrieveSkillUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.keax.skill.infrastructure.in.web.dto.SkillDTO;
import com.keax.skill.domain.ports.in.CreateSkillUseCase;
import com.keax.skill.domain.ports.in.DeleteSkillUseCase;
import com.keax.skill.domain.ports.in.UpdateSkillUseCase;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/skill")
public class SkillController {

    @Autowired
    private CreateSkillUseCase createSkillUseCase;

    @Autowired
    private UpdateSkillUseCase updateSkillUseCase;

    @Autowired
    private RetrieveSkillUseCase retrieveSkillUseCase;

    @Autowired
    private DeleteSkillUseCase deleteSkillUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<SkillDTO>> create(@Valid @RequestBody SkillDTO skill) {
        ApiResponseDTO<SkillDTO> response = new ApiResponseDTO<>(
                true,
                "The skill was created successfully",
                SkillWebMapper.fromDomain(
                        createSkillUseCase.createSkill(SkillWebMapper.toDomain(skill))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<ApiResponseDTO<SkillDTO>> update(@PathVariable Long skillId, @Valid @RequestBody SkillDTO skill) {
        ApiResponseDTO<SkillDTO> response = new ApiResponseDTO<>(
                true,
                "The skill was successfully updated",
                SkillWebMapper.fromDomain(
                        updateSkillUseCase.updateSkill(skillId, SkillWebMapper.toDomain(skill))
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<SkillDTO>>> list() {
        ApiResponseDTO<List<SkillDTO>> response = new ApiResponseDTO<>(
                true,
                "The skills were found successfully",
                retrieveSkillUseCase.getListSkill().stream().map(SkillWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-deleted/{deleted}")
    public ResponseEntity<ApiResponseDTO<List<SkillDTO>>> listByDeleted(@PathVariable Boolean deleted) {
        ApiResponseDTO<List<SkillDTO>> response = new ApiResponseDTO<>(
                true,
                "The skills were found successfully",
                retrieveSkillUseCase.findBySkillDeleted(deleted).stream().map(SkillWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<ApiResponseDTO<SkillDTO>> delete(@PathVariable Long skillId) {
        deleteSkillUseCase.deleteSkill(skillId);

        ApiResponseDTO<SkillDTO> response = new ApiResponseDTO<>(
                true,
                "The skill is successfully eliminated",
                null
        );

        return ResponseEntity.ok(response);
    }

}
