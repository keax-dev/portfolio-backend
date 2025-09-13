package com.keax.application.usecases.UploadImage;

import com.keax.domain.ports.in.UploadImage.UploadImageSkillUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.ports.out.SkillRepositoryPort;
import com.keax.domain.exceptions.ExceptionMessage;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.cloudinary.utils.ObjectUtils;
import com.keax.domain.models.Skill;
import com.cloudinary.Cloudinary;
import java.util.Optional;

@Component
public class UploadImageSkillUseCaseImpl implements UploadImageSkillUseCase {

    @Autowired
    private SkillRepositoryPort skillRepositoryPort;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Skill uploadImageSkill(Long skillId, MultipartFile img) {

        if (img == null || img.isEmpty()){
            throw new ExceptionMessage("The img is required");
        }

        String contentType = img.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp"))) {
            throw new ExceptionMessage("Image format not allowed (JPG, PNG, WEBP only)");
        }

        Optional<Skill> optional = skillRepositoryPort.findBySkillIdAndSkillDeleted(skillId, false);

        if (optional.isEmpty()){
            throw new ExceptionAlert("The skill to be updated does not exist");
        }

        try {
            var resultUpload = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.asMap("folder", "Skills"));
            String imageUrl = resultUpload.get("secure_url").toString();

            Skill skill = optional.get();
            skill.setSkillPicture(imageUrl);

            return skillRepositoryPort.updateSkill(skill);
        } catch (Exception e) {
            throw  new ExceptionAlert("An error occurred while uploading the skill's image");
        }
    }

}
