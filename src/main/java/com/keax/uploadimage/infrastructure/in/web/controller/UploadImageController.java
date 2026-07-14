package com.keax.uploadimage.infrastructure.in.web.controller;

import lombok.RequiredArgsConstructor;

import com.keax.institution.infrastructure.in.web.mapper.InstitutionWebMapper;
import com.keax.uploadimage.infrastructure.in.web.mapper.ImageFileWebMapper;
import com.keax.uploadimage.domain.ports.in.UploadImageInstitutionUseCase;
import com.keax.uploadimage.domain.ports.in.UploadImageProjectUseCase;
import com.keax.uploadimage.domain.ports.in.UploadImageProfileUseCase;
import com.keax.profile.infrastructure.in.web.mapper.ProfileWebMapper;
import com.keax.project.infrastructure.in.web.mapper.ProjectWebMapper;
import com.keax.institution.infrastructure.in.web.dto.InstitutionDTO;
import com.keax.uploadimage.domain.ports.in.UploadImageSkillUseCase;
import com.keax.skill.infrastructure.in.web.mapper.SkillWebMapper;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keax.profile.infrastructure.in.web.dto.ProfileDTO;
import com.keax.project.infrastructure.in.web.dto.ProjectDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.keax.skill.infrastructure.in.web.dto.SkillDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class UploadImageController {
    private final UploadImageInstitutionUseCase uploadImageInstitutionUseCase;
    private final UploadImageProfileUseCase uploadImageProfileUseCase;
    private final UploadImageSkillUseCase uploadImageSkillUseCase;
    private final UploadImageProjectUseCase uploadImageProjectUseCase;

    @PostMapping(
            value = "/institution/{institutionId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponseDTO<InstitutionDTO>> uploadImgInstitution(
            @PathVariable Long institutionId,
            @RequestParam(value = "image", required = false) MultipartFile file
    ) {
        ApiResponseDTO<InstitutionDTO> response = new ApiResponseDTO<>(
                true,
                "The image of the institution has been uploaded successfully",
                InstitutionWebMapper.fromDomain(
                        uploadImageInstitutionUseCase.uploadImageInstitution(institutionId, ImageFileWebMapper.toDomain(file))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponseDTO<ProfileDTO>> uploadImgProfile(
            @RequestParam(value = "image", required = false) MultipartFile file
    ) {
        ApiResponseDTO<ProfileDTO> response = new ApiResponseDTO<>(
                true,
                "The image of the profile has been uploaded successfully",
                ProfileWebMapper.fromDomain(
                        uploadImageProfileUseCase.uploadImageProfile(ImageFileWebMapper.toDomain(file))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/skill/{skillId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponseDTO<SkillDTO>> uploadImgSkill(
            @PathVariable Long skillId,
            @RequestParam(value = "image", required = false) MultipartFile file
    ) {
        ApiResponseDTO<SkillDTO> response = new ApiResponseDTO<>(
                true,
                "The image of the skill has been uploaded successfully",
                SkillWebMapper.fromDomain(
                        uploadImageSkillUseCase.uploadImageSkill(skillId, ImageFileWebMapper.toDomain(file))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/project/{projectId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponseDTO<ProjectDTO>> uploadImgProject(
            @PathVariable Long projectId,
            @RequestParam(value = "image", required = false) MultipartFile file
    ) {
        ApiResponseDTO<ProjectDTO> response = new ApiResponseDTO<>(
                true,
                "The image of the project has been uploaded successfully",
                ProjectWebMapper.fromDomain(
                        uploadImageProjectUseCase.uploadImageProject(projectId, ImageFileWebMapper.toDomain(file))
                )
        );

        return ResponseEntity.ok(response);
    }

}
