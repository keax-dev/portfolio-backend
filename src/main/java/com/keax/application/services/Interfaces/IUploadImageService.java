package com.keax.application.services.Interfaces;

import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.models.Institution;
import com.keax.domain.models.Profile;

public interface IUploadImageService {

    Institution uploadImageInstitution(Long institution_id, MultipartFile img);
    Profile uploadImageProfile(MultipartFile img);

}
