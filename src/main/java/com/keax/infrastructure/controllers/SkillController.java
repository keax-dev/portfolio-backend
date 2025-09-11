package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.ISkillService;
import com.keax.domain.models.Education;
import com.keax.domain.models.Skill;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill")
public class SkillController {

    @Autowired
    private ISkillService skillService;

    @PostMapping
    public ResponseEntity<ApiResponse<Skill>> create(@Valid @RequestBody Skill skill){

        ApiResponse<Skill> response = new ApiResponse<>(
                true,
                "The skill was created successfully",
                skillService.createSkill(skill)
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<ApiResponse<Skill>> update(@PathVariable Long skillId, @Valid @RequestBody Skill skill){

        ApiResponse<Skill> response = new ApiResponse<>(
                true,
                "The skill was successfully updated",
                skillService.updateSkill(skillId, skill)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Skill>>> listEducation(){

        ApiResponse<List<Skill>> response = new ApiResponse<>(
                true,
                "The skills were found successfully",
                skillService.getListSkill()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-deleted/{deleted}")
    public ResponseEntity<ApiResponse<List<Skill>>> listEducationByDeleted(@PathVariable Boolean deleted){

        ApiResponse<List<Skill>> response = new ApiResponse<>(
                true,
                "The skills were found successfully",
                skillService.findBySkillDeleted(deleted)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<ApiResponse<Education>> delete(@PathVariable Long skillId){

        Skill skill = skillService.deleteSkill(skillId);

        ApiResponse<Education> response = new ApiResponse<>(
                true,
                "The skill is successfully eliminated",
                null
        );

        return ResponseEntity.ok(response);
    }

}
