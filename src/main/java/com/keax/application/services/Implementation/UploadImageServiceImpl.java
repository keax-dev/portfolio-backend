package com.keax.application.services.Implementation;

import com.keax.application.services.Interfaces.IUploadImageService;
import com.keax.domain.ports.in.UploadImage.UploadImageUseCase;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Institution;

@Service
public class UploadImageServiceImpl implements IUploadImageService {

    private final UploadImageUseCase uploadImageUseCase;

    public UploadImageServiceImpl(UploadImageUseCase uploadImageUseCase) {
        this.uploadImageUseCase = uploadImageUseCase;
    }

    @Override
    public Institution uploadImageInstitution(Long institution_id, MultipartFile img) {
        return uploadImageUseCase.uploadImageInstitution(institution_id, img);
    }
}
