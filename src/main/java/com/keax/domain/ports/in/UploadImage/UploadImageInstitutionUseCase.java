package com.keax.domain.ports.in.UploadImage;

import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.models.Institution;

public interface UploadImageInstitutionUseCase {

    Institution uploadImageInstitution(Long institutionId,  MultipartFile img);

}
