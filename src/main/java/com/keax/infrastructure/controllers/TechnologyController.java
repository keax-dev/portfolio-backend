package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.ITechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.keax.domain.models.Technology;
import com.keax.domain.models.Education;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/technology")
public class TechnologyController {

    @Autowired
    private ITechnologyService technologyService;

    @PostMapping
    public ResponseEntity<ApiResponse<Technology>> create(@Valid @RequestBody Technology technology){

        ApiResponse<Technology> response = new ApiResponse<>(
                true,
                "The technology was created successfully",
                technologyService.createTechnology(technology)
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{technologyId}")
    public ResponseEntity<ApiResponse<Technology>> update(@PathVariable Long technologyId, @Valid @RequestBody Technology technology){

        ApiResponse<Technology> response = new ApiResponse<>(
                true,
                "The technology was successfully updated",
                technologyService.updateTechnology(technologyId, technology)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Technology>>> list(){

        ApiResponse<List<Technology>> response = new ApiResponse<>(
                true,
                "The technologies were found successfully",
                technologyService.getListTechnology()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-deleted/{deleted}")
    public ResponseEntity<ApiResponse<List<Technology>>> listByDeleted(@PathVariable Boolean deleted){

        ApiResponse<List<Technology>> response = new ApiResponse<>(
                true,
                "The technologies were found successfully",
                technologyService.findByTechnologyDeleted(deleted)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{technologyId}")
    public ResponseEntity<ApiResponse<Education>> delete(@PathVariable Long technologyId){

        Technology technology = technologyService.deleteTechnology(technologyId);

        ApiResponse<Education> response = new ApiResponse<>(
                true,
                "The technology is successfully eliminated",
                null
        );

        return ResponseEntity.ok(response);
    }

}
