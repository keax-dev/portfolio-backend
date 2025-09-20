package com.keax.domain.ports.in.UploadImage;

import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.models.Project;

public interface UploadImageProjectUseCase {

    Project uploadImageProject(Long projectId, MultipartFile img);

}
