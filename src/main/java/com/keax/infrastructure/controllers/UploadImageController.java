package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.IUploadImageService;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import com.keax.domain.models.Institution;
import com.keax.domain.models.Project;
import com.keax.domain.models.Profile;
import com.keax.domain.models.Skill;

@RestController
@RequestMapping("/api/image")
public class UploadImageController {

    @Autowired
    private IUploadImageService uploadImageService;

    @PostMapping(
            value = "/institution/{institutionId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Institution>> uploadImgInstitution(
            @PathVariable Long institutionId,
            @RequestParam(value = "image", required = false) MultipartFile file
    ){

        ApiResponse<Institution> response = new ApiResponse<>(
          true,
          "The image of the institution has been uploaded successfully",
                uploadImageService.uploadImageInstitution(institutionId, file)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Profile>> uploadImgProfile(
            @RequestParam(value = "image", required = false) MultipartFile file
    ){

        ApiResponse<Profile> response = new ApiResponse<>(
                true,
                "The image of the profile has been uploaded successfully",
                uploadImageService.uploadImageProfile(file)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/skill/{skillId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Skill>> uploadImgSkill(
            @PathVariable Long skillId,
            @RequestParam(value = "image", required = false) MultipartFile file
    ){

        ApiResponse<Skill> response = new ApiResponse<>(
                true,
                "The image of the skill has been uploaded successfully",
                uploadImageService.uploadImageSkill(skillId, file)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/project/{projectId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Project>> uploadImgProject(
            @PathVariable Long projectId,
            @RequestParam(value = "image", required = false) MultipartFile file
    ){

        ApiResponse<Project> response = new ApiResponse<>(
                true,
                "The image of the project has been uploaded successfully",
                uploadImageService.uploadImageProject(projectId, file)
        );

        return ResponseEntity.ok(response);
    }

}
