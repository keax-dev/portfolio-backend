package com.keax.domain.ports.in.UploadImage;

import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.models.Skill;

public interface UploadImageSkillUseCase {

    Skill uploadImageSkill(Long skillId, MultipartFile img);

}
