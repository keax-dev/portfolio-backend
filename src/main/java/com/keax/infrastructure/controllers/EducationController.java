package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.IEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.keax.domain.models.Education;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    @Autowired
    private IEducationService educationService;

    @PostMapping
    public ResponseEntity<ApiResponse<Education>> create(@Valid @RequestBody Education education){

        ApiResponse<Education> response = new ApiResponse<>(
                true,
                "Education was created successfully",
                educationService.createEducation(education)
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{education_id}")
    public ResponseEntity<ApiResponse<Education>> update(@PathVariable Long education_id, @Valid @RequestBody Education education){

        ApiResponse<Education> response = new ApiResponse<>(
                true,
                "Education was successfully updated",
                educationService.updateEducation(education_id, education)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Education>>> listEducation(){

        ApiResponse<List<Education>> response = new ApiResponse<>(
                true,
                "The educations were found successfully",
                educationService.getListEducation()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-deleted/{deleted}")
    public ResponseEntity<ApiResponse<List<Education>>> listEducationByDeleted(@PathVariable Boolean deleted){

        ApiResponse<List<Education>> response = new ApiResponse<>(
                true,
                "The educations were found successfully",
                educationService.findByEducationDeleted(deleted)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{education_id}")
    public ResponseEntity<ApiResponse<Education>> delete(@PathVariable Long education_id){

        Education education = educationService.deleteEducation(education_id);

        ApiResponse<Education> response = new ApiResponse<>(
                true,
                "Education is successfully eliminated",
                null
        );

        return ResponseEntity.ok(response);
    }

}
