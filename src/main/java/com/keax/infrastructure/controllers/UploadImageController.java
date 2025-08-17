package com.keax.infrastructure.controllers;

import com.keax.application.services.Interfaces.IUploadImageService;
import com.keax.domain.models.Institution;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
public class UploadImageController {

    private final IUploadImageService uploadImageService;

    public UploadImageController(IUploadImageService uploadImageService) {
        this.uploadImageService = uploadImageService;
    }

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

}
