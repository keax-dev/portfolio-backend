package com.keax.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import com.keax.application.services.Interfaces.*;
import org.springframework.http.ResponseEntity;
import com.keax.domain.models.*;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private IProfileService profileService;

    @Autowired
    private IEducationService educationService;

    @Autowired
    private ISkillService skillService;

    @Autowired
    private ITechnologyService technologyService;

    @Autowired
    private ISocialNetworkService socialNetworkService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Profile>> getProfile(){

        ApiResponse<Profile> response = new ApiResponse<>(
                true,
                "Profile information found successfully",
                profileService.getProfile()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/education")
    public ResponseEntity<ApiResponse<List<Education>>> getEducation(){

        ApiResponse<List<Education>> response = new ApiResponse<>(
                true,
                "Educational information found successfully",
                educationService.findByEducationDeleted(false)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/skill")
    public ResponseEntity<ApiResponse<List<Skill>>> getSkill(){

        ApiResponse<List<Skill>> response = new ApiResponse<>(
                true,
                "Skill information found successfully",
                skillService.findBySkillDeleted(false)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/technology")
    public ResponseEntity<ApiResponse<List<Technology>>> getTechnology(){

        ApiResponse<List<Technology>> response = new ApiResponse<>(
                true,
                "Technology information found successfully",
                technologyService.findByTechnologyDeletedWithProjects(false)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/socialNetwork")
    public ResponseEntity<ApiResponse<List<SocialNetwork>>> getSocialNetwork(){

        ApiResponse<List<SocialNetwork>> response = new ApiResponse<>(
                true,
                "Social Network information found successfully",
                socialNetworkService.findBySocialNetworkDeleted(false)
        );

        return ResponseEntity.ok(response);
    }

}
