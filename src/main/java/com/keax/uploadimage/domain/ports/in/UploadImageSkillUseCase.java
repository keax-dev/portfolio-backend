package com.keax.uploadimage.domain.ports.in;

import com.keax.uploadimage.domain.model.ImageFile;
import com.keax.skill.domain.model.Skill;

public interface UploadImageSkillUseCase {

    Skill uploadImageSkill(Long skillId, ImageFile img);

}
