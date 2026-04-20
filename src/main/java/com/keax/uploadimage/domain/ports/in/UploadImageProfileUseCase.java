package com.keax.uploadimage.domain.ports.in;

import com.keax.uploadimage.domain.model.ImageFile;
import com.keax.profile.domain.model.Profile;

public interface UploadImageProfileUseCase {

    Profile uploadImageProfile(ImageFile img);

}
