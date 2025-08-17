package com.keax.domain.ports.in.UploadImage;

import com.keax.domain.models.Institution;
import org.springframework.web.multipart.MultipartFile;

public interface UploadImageUseCase {
    Institution uploadImageInstitution(Long institution_id,  MultipartFile img);
}
