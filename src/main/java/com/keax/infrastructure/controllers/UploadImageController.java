package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.IUploadImageService;
import com.keax.domain.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import com.keax.domain.models.Institution;

@RestController
@RequestMapping("/api/image")
public class UploadImageController {

    @Autowired
    private IUploadImageService uploadImageService;

    @PostMapping(
            value = "/institution/{institution_id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Institution>> uploadImgInstitution(
            @PathVariable Long institution_id,
            @RequestParam(value = "image", required = false) MultipartFile file
    ){

        ApiResponse<Institution> response = new ApiResponse<>(
          true,
          "The image of the institution has been uploaded successfully",
                uploadImageService.uploadImageInstitution(institution_id, file)
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
                "The image of the institution has been uploaded successfully",
                uploadImageService.uploadImageProfile(file)
        );

        return ResponseEntity.ok(response);
    }

}
