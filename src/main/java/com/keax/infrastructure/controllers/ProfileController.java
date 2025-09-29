package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.keax.domain.models.Profile;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private IProfileService profileService;

    @PostMapping
    public ResponseEntity<ApiResponse<Profile>> create(@Valid @RequestBody Profile profile){

        ApiResponse<Profile> response = new ApiResponse<>(
                true,
                "The profile was created correctly",
                profileService.createProfile(profile)
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Profile>> update(@Valid @RequestBody Profile profile){

        ApiResponse<Profile> response = new ApiResponse<>(
                true,
                "The profile was updated correctly",
                profileService.updateProfile(profile)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Profile>> get(){

        ApiResponse<Profile> response = new ApiResponse<>(
                true,
                "Profile information found successfully",
                profileService.getProfile()
        );

        return ResponseEntity.ok(response);
    }

}
