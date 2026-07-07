package com.keax.socialnetwork.infrastructure.in.web.controller;

import com.keax.socialnetwork.infrastructure.in.web.mapper.SocialNetworkWebMapper;
import com.keax.socialnetwork.domain.ports.in.RetrieveSocialNetworkUseCase;
import com.keax.socialnetwork.domain.ports.in.CreateSocialNetworkUseCase;
import com.keax.socialnetwork.domain.ports.in.DeleteSocialNetworkUseCase;
import com.keax.socialnetwork.domain.ports.in.UpdateSocialNetworkUseCase;
import com.keax.socialnetwork.infrastructure.in.web.dto.SocialNetworkDTO;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/socialNetwork")
public class SocialNetworkController {

    @Autowired
    private CreateSocialNetworkUseCase createSocialNetworkUseCase;

    @Autowired
    private UpdateSocialNetworkUseCase updateSocialNetworkUseCase;

    @Autowired
    private RetrieveSocialNetworkUseCase retrieveSocialNetworkUseCase;

    @Autowired
    private DeleteSocialNetworkUseCase deleteSocialNetworkUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<SocialNetworkDTO>> create(@Valid @RequestBody SocialNetworkDTO socialNetwork) {
        ApiResponseDTO<SocialNetworkDTO> response = new ApiResponseDTO<>(
                true,
                "The social network was created successfully",
                SocialNetworkWebMapper.fromDomain(
                        createSocialNetworkUseCase.createSocialNetwork(SocialNetworkWebMapper.toDomain(socialNetwork))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{socialNetworkId}")
    public ResponseEntity<ApiResponseDTO<SocialNetworkDTO>> update(@PathVariable Long socialNetworkId, @Valid @RequestBody SocialNetworkDTO socialNetwork) {
        ApiResponseDTO<SocialNetworkDTO> response = new ApiResponseDTO<>(
                true,
                "The social network was successfully updated",
                SocialNetworkWebMapper.fromDomain(
                        updateSocialNetworkUseCase.updateSocialNetwork(socialNetworkId, SocialNetworkWebMapper.toDomain(socialNetwork))
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<SocialNetworkDTO>>> list() {
        ApiResponseDTO<List<SocialNetworkDTO>> response = new ApiResponseDTO<>(
                true,
                "Social networks have been found",
                retrieveSocialNetworkUseCase.getListSocialNetwork().stream().map(SocialNetworkWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{socialNetworkId}")
    public ResponseEntity<ApiResponseDTO<SocialNetworkDTO>> delete(@PathVariable Long socialNetworkId) {
        deleteSocialNetworkUseCase.deleteSocialNetwork(socialNetworkId);

        ApiResponseDTO<SocialNetworkDTO> response = new ApiResponseDTO<>(
                true,
                "The social network was removed",
                null
        );

        return ResponseEntity.ok(response);
    }

}
