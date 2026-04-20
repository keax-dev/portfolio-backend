package com.keax.uploadimage.domain.ports.in;

import com.keax.institution.domain.model.Institution;
import com.keax.uploadimage.domain.model.ImageFile;

public interface UploadImageInstitutionUseCase {

    Institution uploadImageInstitution(Long institutionId, ImageFile img);

}
