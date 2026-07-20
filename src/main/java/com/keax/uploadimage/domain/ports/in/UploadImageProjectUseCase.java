package com.keax.uploadimage.domain.ports.in;

import com.keax.uploadimage.domain.model.ImageFile;
import com.keax.project.domain.model.Project;
import java.util.List;

public interface UploadImageProjectUseCase {

    Project uploadProjectImages(Long projectId, List<ImageFile> images);

    Project deleteProjectImage(Long projectId, Long projectImageId);

}
