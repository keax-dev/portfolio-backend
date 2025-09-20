package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.IInstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.keax.domain.models.Institution;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/institution")
public class InstitutionController {

    @Autowired
    private IInstitutionService institutionServiceImpl;

    @PostMapping
    public ResponseEntity<ApiResponse<Institution>> create(@Valid @RequestBody Institution institution){

        ApiResponse<Institution> response = new ApiResponse<>(
                true,
                "The institution was successfully created",
                institutionServiceImpl.createInstitution(institution)
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{institution_id}")
    public ResponseEntity<ApiResponse<Institution>> update(@PathVariable Long institution_id, @Valid @RequestBody Institution institution){
        ApiResponse<Institution> response = new ApiResponse<>(
                true,
                "The institution was successfully updated",
                institutionServiceImpl.updateInstitution(institution_id, institution)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Institution>>> list(){

        ApiResponse<List<Institution>> response = new ApiResponse<>(
                true,
                "The institutions were successfully found",
                institutionServiceImpl.getListInstitution()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-deleted/{deleted}")
    public ResponseEntity<ApiResponse<List<Institution>>> listByDeleted(@PathVariable Boolean deleted){

        ApiResponse<List<Institution>> response = new ApiResponse<>(
                true,
                "The institutions were successfully found",
                institutionServiceImpl.findByInstitutionDeleted(deleted)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{institution_id}")
    private ResponseEntity<?> delete(@PathVariable Long institution_id){

        institutionServiceImpl.deleteInstitution(institution_id);

        ApiResponse<Boolean> response = new ApiResponse<>(
                true,
                "The institution was successfully eliminated",
                null
        );

        return ResponseEntity.ok(response);
    }

}
