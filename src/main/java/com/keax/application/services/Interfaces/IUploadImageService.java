package com.keax.application.services.Interfaces;

import com.keax.domain.models.Project;
import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.models.Institution;
import com.keax.domain.models.Profile;
import com.keax.domain.models.Skill;

public interface IUploadImageService {

    Institution uploadImageInstitution(Long institutionId, MultipartFile img);
    Profile uploadImageProfile(MultipartFile img);
    Skill uploadImageSkill(Long skillId, MultipartFile img);
    Project uploadImageProject(Long projectId, MultipartFile img);

}
