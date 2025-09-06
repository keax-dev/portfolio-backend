package com.keax.application.services.Implementation;

import com.keax.application.services.Interfaces.IUploadImageService;
import com.keax.domain.ports.in.UploadImage.UploadImageInstitutionUseCase;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Institution;

@Service
public class UploadImageServiceImpl implements IUploadImageService {

    private final UploadImageInstitutionUseCase uploadImageInstitutionUseCase;

    public UploadImageServiceImpl(UploadImageInstitutionUseCase uploadImageInstitutionUseCase) {
        this.uploadImageInstitutionUseCase = uploadImageInstitutionUseCase;
    }

    @Override
    public Institution uploadImageInstitution(Long institution_id, MultipartFile img) {
        return uploadImageInstitutionUseCase.uploadImageInstitution(institution_id, img);
    }
}
