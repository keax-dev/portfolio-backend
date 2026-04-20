package com.keax.uploadimage.application.usecases;

import com.keax.uploadimage.domain.ports.in.UploadImageSkillUseCase;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.skill.domain.model.Skill;

@Service
@Transactional
public class UploadImageSkillUseCaseImpl implements UploadImageSkillUseCase {

    @Autowired
    private SkillRepositoryPort skillRepositoryPort;

    @Autowired
    private ImageStoragePort imageStoragePort;

    @Override
    public Skill uploadImageSkill(Long skillId, ImageFile img) {

        validateImage(img, "The img is required");

        Skill skill = skillRepositoryPort.findBySkillIdAndSkillDeleted(
                skillId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The skill to be updated does not exist")
        );

        try {
            skill.setSkillPicture(
                    imageStoragePort.upload(img, "Skills")
            );

            return skillRepositoryPort.updateSkill(skill);
        } catch (Exception e) {
            throw new ExceptionAlert("An error occurred while uploading the skill's image");
        }
    }

    private void validateImage(ImageFile img, String requiredMessage) {
        if (img == null || img.isEmpty()) {
            throw new ExceptionMessage(requiredMessage);
        }

        String contentType = img.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp"))) {
            throw new ExceptionMessage("Image format not allowed (JPG, PNG, WEBP only)");
        }
    }

}
