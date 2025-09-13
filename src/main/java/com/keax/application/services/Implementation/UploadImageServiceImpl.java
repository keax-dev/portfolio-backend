package com.keax.application.services.Implementation;

import com.keax.domain.ports.in.UploadImage.UploadImageInstitutionUseCase;
import com.keax.domain.ports.in.UploadImage.UploadImageProfileUseCase;
import com.keax.application.services.Interfaces.IUploadImageService;
import com.keax.domain.ports.in.UploadImage.UploadImageSkillUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Institution;
import com.keax.domain.models.Profile;
import com.keax.domain.models.Skill;

@Service
public class UploadImageServiceImpl implements IUploadImageService {

    @Autowired
    private UploadImageInstitutionUseCase uploadImageInstitutionUseCase;

    @Autowired
    private UploadImageProfileUseCase uploadImageProfileUseCase;

    @Autowired
    private UploadImageSkillUseCase uploadImageSkillUseCase;

    @Override
    public Institution uploadImageInstitution(Long institution_id, MultipartFile img) {
        return uploadImageInstitutionUseCase.uploadImageInstitution(institution_id, img);
    }

    @Override
    public Profile uploadImageProfile(MultipartFile img) {
        return uploadImageProfileUseCase.uploadImageProfile(img);
    }

    @Override
    public Skill uploadImageSkill(Long skillId, MultipartFile img) {
        return uploadImageSkillUseCase.uploadImageSkill(skillId, img);
    }

}
