package com.keax.profile.infrastructure.in.web.controller;

import com.keax.profile.infrastructure.in.web.mapper.ProfileWebMapper;
import com.keax.profile.domain.ports.in.RetrieveProfileUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keax.profile.domain.ports.in.CreateProfileUseCase;
import com.keax.profile.domain.ports.in.UpdateProfileUseCase;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import com.keax.profile.infrastructure.in.web.dto.ProfileDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private CreateProfileUseCase createProfileUseCase;

    @Autowired
    private UpdateProfileUseCase updateProfileUseCase;

    @Autowired
    private RetrieveProfileUseCase retrieveProfileUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProfileDTO>> create(@Valid @RequestBody ProfileDTO profile) {
        ApiResponseDTO<ProfileDTO> response = new ApiResponseDTO<>(
                true,
                "The profile was created correctly",
                ProfileWebMapper.fromDomain(
                        createProfileUseCase.createProfile(ProfileWebMapper.toDomain(profile))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponseDTO<ProfileDTO>> update(@Valid @RequestBody ProfileDTO profile) {
        ApiResponseDTO<ProfileDTO> response = new ApiResponseDTO<>(
                true,
                "The profile was updated correctly",
                ProfileWebMapper.fromDomain(
                        updateProfileUseCase.updateProfile(ProfileWebMapper.toDomain(profile))
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<ProfileDTO>> get() {
        ApiResponseDTO<ProfileDTO> response = new ApiResponseDTO<>(
                true,
                "Profile information found successfully",
                ProfileWebMapper.fromDomain(retrieveProfileUseCase.getProfile())
        );

        return ResponseEntity.ok(response);
    }

}
