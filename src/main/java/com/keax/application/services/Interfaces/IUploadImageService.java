package com.keax.application.services.Interfaces;

import com.keax.domain.models.Institution;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadImageService {
    Institution uploadImageInstitution(Long institution_id, MultipartFile img);
}
