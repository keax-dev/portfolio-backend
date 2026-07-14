package com.keax.uploadimage.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.uploadimage.application.validation.ImageFileValidator;
import com.keax.uploadimage.domain.ports.in.UploadImageSkillUseCase;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.skill.domain.model.Skill;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadImageSkillUseCaseImpl implements UploadImageSkillUseCase {
    private final SkillRepositoryPort skillRepositoryPort;
    private final ImageStoragePort imageStoragePort;

    @Override
    public Skill uploadImageSkill(Long skillId, ImageFile img) {

        ImageFileValidator.validate(img, "The img is required");

        Skill skill = skillRepositoryPort.findBySkillIdAndSkillDeleted(
                skillId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The skill to be updated does not exist")
        );

        String oldImageUrl = skill.getSkillPicture();
        String newImageUrl = null;

        try {
            newImageUrl = imageStoragePort.upload(img, "Skills");
            skill.setSkillPicture(newImageUrl);

            Skill updatedSkill = skillRepositoryPort.updateSkill(skill);
            imageStoragePort.delete(oldImageUrl);
            return updatedSkill;
        } catch (Exception e) {
            imageStoragePort.delete(newImageUrl);
            throw new ExternalServiceException("An error occurred while uploading the skill's image", e);
        }
    }

}
