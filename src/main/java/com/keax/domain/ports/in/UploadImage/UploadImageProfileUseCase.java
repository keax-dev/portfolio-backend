package com.keax.domain.ports.in.UploadImage;

import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.models.Profile;

public interface UploadImageProfileUseCase {

    Profile uploadImageProfile(MultipartFile img);

}
