package com.keax.domain.ports.in.UploadImage;

import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.models.Institution;

public interface UploadImageUseCase {

    Institution uploadImageInstitution(Long institution_id,  MultipartFile img);

}
