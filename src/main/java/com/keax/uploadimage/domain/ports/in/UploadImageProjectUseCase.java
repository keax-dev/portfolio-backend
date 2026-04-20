package com.keax.uploadimage.domain.ports.in;

import com.keax.uploadimage.domain.model.ImageFile;
import com.keax.project.domain.model.Project;

public interface UploadImageProjectUseCase {

    Project uploadImageProject(Long projectId, ImageFile img);

}
