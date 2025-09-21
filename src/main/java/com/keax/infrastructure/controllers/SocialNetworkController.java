package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.ISocialNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.keax.domain.models.SocialNetwork;
import com.keax.domain.models.Project;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/socialNetwork")
public class SocialNetworkController {

    @Autowired
    private ISocialNetworkService socialNetworkService;

    @PostMapping
    public ResponseEntity<ApiResponse<SocialNetwork>> create(@Valid @RequestBody SocialNetwork socialNetwork){

        ApiResponse<SocialNetwork> response = new ApiResponse<>(
                true,
                "The social network was created successfully",
                socialNetworkService.createSocialNetwork(socialNetwork)
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{socialNetworkId}")
    public ResponseEntity<ApiResponse<SocialNetwork>> update(@PathVariable Long socialNetworkId, @Valid @RequestBody SocialNetwork socialNetwork){

        ApiResponse<SocialNetwork> response = new ApiResponse<>(
                true,
                "The social network was successfully updated",
                socialNetworkService.updateSocialNetwork(socialNetworkId, socialNetwork)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SocialNetwork>>> list(){

        ApiResponse<List<SocialNetwork>> response = new ApiResponse<>(
                true,
                "Social networks have been found",
                socialNetworkService.getListSocialNetwork()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-deleted/{deleted}")
    public ResponseEntity<ApiResponse<List<SocialNetwork>>> listByDeleted(@PathVariable Boolean deleted){

        ApiResponse<List<SocialNetwork>> response = new ApiResponse<>(
                true,
                "Social networks have been found",
                socialNetworkService.findBySocialNetworkDeleted(deleted)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Project>> delete(@PathVariable Long projectId){

        SocialNetwork socialNetwork = socialNetworkService.deleteSocialNetwork(projectId);

        ApiResponse<Project> response = new ApiResponse<>(
                true,
                "The social network was removed",
                null
        );

        return ResponseEntity.ok(response);
    }

}
