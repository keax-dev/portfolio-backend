package com.keax.uploadimage.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.uploadimage.application.validation.ImageFileValidator;
import com.keax.uploadimage.domain.ports.in.UploadImageSkillUseCase;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import com.keax.uploadimage.application.services.ImagePersistenceCoordinator;
import com.keax.uploadimage.application.services.ImageCleanupProcessor;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.stereotype.Service;
import com.keax.skill.domain.model.Skill;

@Service
@RequiredArgsConstructor
public class UploadImageSkillUseCaseImpl implements UploadImageSkillUseCase {
    private final SkillRepositoryPort skillRepositoryPort;
    private final ImageStoragePort imageStoragePort;
    private final ImagePersistenceCoordinator imagePersistenceCoordinator;
    private final ImageCleanupProcessor imageCleanupProcessor;

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
        String newImageUrl = imageStoragePort.upload(img, "Skills");
        skill.setSkillPicture(newImageUrl);
        java.util.List<String> obsoleteUrls = oldImageUrl == null || oldImageUrl.isBlank()
                ? java.util.List.of()
                : java.util.List.of(oldImageUrl);
        Skill updatedSkill;
        try {
            updatedSkill = imagePersistenceCoordinator.updateSkill(
                    skill,
                    obsoleteUrls
            );
        } catch (RuntimeException ex) {
            imageCleanupProcessor.deleteOrEnqueue(java.util.List.of(newImageUrl));
            throw ex;
        }
        imageCleanupProcessor.processQueuedUrls(obsoleteUrls);
        return updatedSkill;
    }

}
